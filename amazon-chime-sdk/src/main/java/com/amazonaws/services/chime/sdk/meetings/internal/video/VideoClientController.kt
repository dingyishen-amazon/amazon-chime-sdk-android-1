/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.chime.sdk.meetings.internal.video

import com.amazonaws.services.chime.sdk.meetings.audiovideo.PrimaryMeetingPromotionObserver
import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.LocalVideoConfiguration
import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.RemoteVideoSource
import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.VideoFrame
import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.VideoSource
import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.VideoSubscriptionConfiguration
import com.amazonaws.services.chime.sdk.meetings.audiovideo.video.capture.DefaultCameraCaptureSource
import com.amazonaws.services.chime.sdk.meetings.device.MediaDevice
import com.amazonaws.services.chime.sdk.meetings.session.MeetingSessionConfiguration
import com.amazonaws.services.chime.sdk.meetings.session.MeetingSessionCredentials

/**
 * [VideoClientController] uses the Video Client for video related functionality such as starting
 * local or remote video, switching camera, or pausing a remote video.
 */
interface VideoClientController {
    /**
     * Start the video client.
     */
    fun start()

    /**
     * Stop and destroy the video client.
     */
    fun stopAndDestroy()

    /**
     * Start local video and begin transmitting frames from an internally held [DefaultCameraCaptureSource].
     * [stopLocalVideo] will stop the internal capture source if being used.
     *
     * Calling this after passing in a custom [VideoSource] will replace it with the internal capture source.
     *
     * This function will only have effect if [start] has already been called
     */
    fun startLocalVideo()

    /**
     * Start local video with configuration and begin transmitting frames from an internally held [DefaultCameraCaptureSource].
     * [stopLocalVideo] will stop the internal capture source if being used.
     *
     * Calling this after passing in a custom [VideoSource] will replace it with the internal capture source.
     *
     * This function will only have effect if [start] has already been called.
     * Calling this function repeatedly will update configuration of local video.
     * If maxBitRateKbps is not set, it will be self adjusted depending on number of users and videos in the meeting
     *
     * @param config: [LocalVideoConfiguration] - The configuration of emitted video stream, e.g. maxBitRateKbps
     */
    fun startLocalVideo(config: LocalVideoConfiguration)

    /**
     * Start local video with a provided custom [VideoSource] which can be used to provide custom
     * [VideoFrame]s to be transmitted to remote clients. This will call [VideoSource.addVideoSink]
     * on the provided source.
     *
     * Calling this function repeatedly will replace the previous [VideoSource] as the one being
     * transmitted. It will also stop and replace the internal capture source if [startLocalVideo]
     * was previously called with no arguments.
     *
     * This function will only have effect if [start] has already been called
     *
     * @param source: [VideoSource] - The source of video frames to be sent to other clients
     */
    fun startLocalVideo(source: VideoSource)

    /**
     * Start local video with with configuration and a provided custom [VideoSource] which can be used to provide custom
     * [VideoFrame] objects to be transmitted to remote clients
     *
     * Calling this function repeatedly will replace the previous [VideoSource] as the one being
     * transmitted. It will update configuration of local video. It will also stop and replace the internal capture source
     * if [startLocalVideo] was called with no arguments.
     * If maxBitRateKbps is not set, it will be self adjusted depending on number of users and videos in the meeting
     *
     * Read [custom video guide](https://github.com/aws/amazon-chime-sdk-android/blob/master/guides/custom_video.md) for details.
     *
     * @param source: [VideoSource] - The source of video frames to be sent to other clients
     * @param config: [LocalVideoConfiguration] - The configuration of emitted video stream, e.g. maxBitRateKbps
     */
    fun startLocalVideo(source: VideoSource, config: LocalVideoConfiguration)

    /**
     * Stops sending video for local attendee. This will additionally stop the internal capture source if being used.
     * If using a custom video source, this will call [VideoSource.removeVideoSink] on the previously provided source.
     */
    fun stopLocalVideo()

    /**
     * Starts receiving video from remote attendee(s).
     */
    fun startRemoteVideo()

    /**
     * Stops receiving video from remote attendee(s).
     */
    fun stopRemoteVideo()

    /**
     * Get the currently active camera, if any. This will return null if using a custom source,
     * e.g. one passed in via [startLocalVideo]
     *
     * @return [MediaDevice] - Information about the current active device used for video.
     */
    fun getActiveCamera(): MediaDevice?

    /**
     * Switches the currently active camera. This will no-op if using a custom source,
     * e.g. one passed in via [startLocalVideo]
     */
    fun switchCamera()

    /**
     * Pause or resume a remote video tile.
     *
     * @param isPaused: Boolean - Whether or not the tile should be paused.
     * @param videoId: Int - Id of the remote video tile to pause or resume.
     */
    fun setRemotePaused(isPaused: Boolean, videoId: Int)

    /**
     * Get the meeting session configuration
     *
     * @return [MeetingSessionConfiguration] - Configuration of current meeting session
     */
    fun getConfiguration(): MeetingSessionConfiguration

    /**
     * Send message via data channel
     *
     * @param topic: String - topic the message is sent to
     * @param data: Any - data payload, it can be ByteArray, String or other serializable object,
     * which will be convert to ByteArray
     * @param lifetimeMs: Int - the milliseconds of lifetime that is available to late subscribers
     * @throws [InvalidParameterException] when topic is not match regex `^[a-zA-Z0-9_-]{1,36}$`,
     * or data size is over 2kb, or lifetime ms is negative
     */
    fun sendDataMessage(topic: String, data: Any, lifetimeMs: Int)

    /**
     * Update remote video source subscription configurations, perform internal conversions.
     *
     * @param addedOrUpdated: Map<RemoteVideoSource, VideoSubscriptionConfiguration>
     * @param removed: Array<RemoteVideoSource>
     */
    fun updateVideoSourceSubscriptions(addedOrUpdated: Map<RemoteVideoSource, VideoSubscriptionConfiguration>, removed: Array<RemoteVideoSource>)

    /**
     * See [AudioVideoFacade.promoteToPrimaryMeeting]
     */
    fun promoteToPrimaryMeeting(credentials: MeetingSessionCredentials, observer: PrimaryMeetingPromotionObserver)

    /**
     * See [AudioVideoFacade.demoteFromPrimaryMeeting]
     */
    fun demoteFromPrimaryMeeting()
}
