package com.eric.statemachine

fun main() {
    val player = MediaPlayer()

    println("当前状态: ${player.stateMachine.state}")
    player.stateMachine.transition(MediaPlayerEvent.Play)
    println("当前状态: ${player.stateMachine.state}")

    player.stateMachine.transition(MediaPlayerEvent.Pause)
    println("当前状态: ${player.stateMachine.state}")

    player.stateMachine.transition(MediaPlayerEvent.Stop)
    println("当前状态: ${player.stateMachine.state}")
}