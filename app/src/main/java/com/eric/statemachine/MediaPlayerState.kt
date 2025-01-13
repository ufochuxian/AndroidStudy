package com.eric.statemachine

import android.util.Log
import com.tinder.StateMachine


sealed class MediaPlayerState {
    object Stopped : MediaPlayerState()
    object Playing : MediaPlayerState()
    object Paused : MediaPlayerState()
}

sealed class MediaPlayerEvent {
    object Play : MediaPlayerEvent()
    object Pause : MediaPlayerEvent ()
    object Stop : MediaPlayerEvent()
}

class MediaPlayer {

    val stateMachine = StateMachine.create<MediaPlayerState, MediaPlayerEvent, Unit> {
        
        // 初始状态
        initialState(MediaPlayerState.Stopped)

        // 定义状态及其转换规则
        state<MediaPlayerState.Stopped> {
            on<MediaPlayerEvent.Play> {
                onEnter { println("进入play状态") }
                onExit { println("离开stop状态") }
                transitionTo(MediaPlayerState.Playing, sideEffect = run {
                    println("播放图片")
                })
            }
        }

        state<MediaPlayerState.Playing> {
            on<MediaPlayerEvent.Pause> {
                transitionTo(MediaPlayerState.Paused)
            }
            on<MediaPlayerEvent.Stop> {
                transitionTo(MediaPlayerState.Stopped)
            }
        }

        state<MediaPlayerState.Paused> {
            on<MediaPlayerEvent.Play> {
                transitionTo(MediaPlayerState.Playing)
            }
            on<MediaPlayerEvent.Stop> {
                transitionTo(MediaPlayerState.Stopped)
            }
        }

        // 日志记录
        onTransition { transition ->
            if (transition is StateMachine.Transition.Valid) {
                println("State changed from ${transition.fromState} to ${transition.toState}")
            }
        }
    }
}
