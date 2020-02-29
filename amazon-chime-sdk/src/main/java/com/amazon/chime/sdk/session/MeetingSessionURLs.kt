/*
 * Copyright (c) 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazon.chime.sdk.session

/**
 * [[MeetingSessionURLs]] contains the URLs that will be used to reach the
 * meeting service.
 */
data class MeetingSessionURLs(
    val audioFallbackURL: String,
    val audioHostURL: String,
    val turnControlURL: String,
    val signalingURL: String
)
