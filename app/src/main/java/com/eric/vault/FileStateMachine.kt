import com.eric.vault.FileState
import com.tinder.StateMachine

class FileStateMachine(private val filePath: String) {

    // 定义目录
    private val normalDir = "/storage/emulated/0/normal/"
    private val hideDir = "/storage/emulated/0/hide/"
    private val trashDir = "/storage/emulated/0/trash/"

    // 获取状态对应的目录
    private fun getDirectory(state: FileState): String {
        return when (state) {
            FileState.NORMAL -> normalDir
            FileState.HIDE -> hideDir
            FileState.DELETE -> trashDir
        }
    }

    // 当前状态机实例
    val stateMachine = StateMachine.create<FileState, Event, Unit> {
        initialState(FileState.NORMAL)  // 设置初始状态

        // 定义 NORMAL 状态
        state<FileState.NORMAL> {
            on<Event.OnHide> {
                transitionTo(FileState.HIDE)
            }
            on<Event.OnDelete> {
                transitionTo(FileState.DELETE)
            }
        }

        // 定义 HIDE 状态
        state<FileState.HIDE> {
            on<Event.OnUnhide> {
                transitionTo(FileState.NORMAL)
            }
            on<Event.OnDelete> {
                transitionTo(FileState.DELETE)
            }
        }

        // 定义 DELETE 状态
        state<FileState.DELETE> {
            on<Event.OnRestore> {
                transitionTo(FileState.NORMAL)
            }
        }
    }

    // 文件移动逻辑
    private fun moveFile(currentState: FileState, targetState: FileState): Boolean {
        val sourcePath = getDirectory(currentState) + filePath
        val destinationPath = getDirectory(targetState) + filePath

        // 实际文件移动（伪代码）
        println("Moving file from $sourcePath to $destinationPath")
        return true  // 假设移动成功
    }

    // 转换状态
    fun transition(event: Event) {
        stateMachine.transition(event)
    }

    // 获取当前状态
    fun getCurrentState(): FileState {
        return stateMachine.state
    }
}

// 定义事件类型
sealed class Event {
    object OnHide : Event()       // 隐藏文件
    object OnDelete : Event()     // 删除文件
    object OnUnhide : Event()     // 取消隐藏
    object OnRestore : Event()    // 恢复删除的文件
}

