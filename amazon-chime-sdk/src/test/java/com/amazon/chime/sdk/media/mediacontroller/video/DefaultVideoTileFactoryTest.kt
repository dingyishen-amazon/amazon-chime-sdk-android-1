/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazon.chime.sdk.media.mediacontroller.video

import com.amazon.chime.sdk.utils.logger.ConsoleLogger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DefaultVideoTileFactoryTest {
    @Test
    fun `makeTile should return object with data from parameters`() {
        val testTileId = 1
        val testAttendeeId = "attendeeId"
        val testLogger = ConsoleLogger()
        val videoTileFactory = DefaultVideoTileFactory(testLogger)

        val testOutput: VideoTile = videoTileFactory.makeTile(testTileId, testAttendeeId)

        assertNotNull(testOutput)
        assertEquals(testTileId, testOutput.state.tileId)
        assertEquals(testAttendeeId, testOutput.state.attendeeId)
    }
}
