package com.jmolsmobile.landscapevideocapture.preview;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import android.view.SurfaceHolder;

import com.jmolsmobile.landscapevideocapture.MockitoTestCase;
import com.jmolsmobile.landscapevideocapture.recorder.CameraWrapper;

public class CapturePreviewTest extends MockitoTestCase {

	private final CameraWrapper	mCameraWrapper	= null;

	public void test_shouldInitializeSurfaceHolder() throws Exception {
		final SurfaceHolder mockHolder = mock(SurfaceHolder.class);
		final CapturePreview preview = new CapturePreview(null, mCameraWrapper, mockHolder, 0, 0);

		verify(mockHolder, times(1)).addCallback(preview);
		verify(mockHolder, times(1)).setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void test_shouldNotStopPreviewWhenPreviewNotRunning() throws Exception {
		final CameraWrapper mockWrapper = mock(CameraWrapper.class);
		final CapturePreview preview = new CapturePreview(null, mockWrapper, mock(SurfaceHolder.class), 0, 0);

		preview.releasePreviewResources();

		verify(mockWrapper, never()).stopPreview();
	}

	public void test_shouldStopPreviewWhenPreviewRunning() throws Exception {
		final CameraWrapper mockWrapper = mock(CameraWrapper.class);
		final CapturePreview preview = new CapturePreview(null, mockWrapper, mock(SurfaceHolder.class), 0, 0);
		preview.setPreviewRunning(true);

		preview.releasePreviewResources();

		verify(mockWrapper, times(1)).stopPreview();
	}

	public void test_shouldCallInterfaceWhenSettingParametersFails() throws Exception {
		final CapturePreviewInterface mockInterface = mock(CapturePreviewInterface.class);
		final CameraWrapper mockWrapper = mock(CameraWrapper.class);
		doThrow(new RuntimeException()).when(mockWrapper).configureForPreview(anyInt(), anyInt());
		createCapturePreviewAndCallSurfaceChanged(mockInterface, mockWrapper);

		verify(mockInterface, times(1)).onCapturePreviewFailed();
	}

	public void test_shouldCallInterfaceWhenStartPreviewFails1() throws Exception {
		final CapturePreviewInterface mockInterface = mock(CapturePreviewInterface.class);
		final CameraWrapper mockWrapper = mock(CameraWrapper.class);
		doThrow(new IOException()).when(mockWrapper).startPreview(any(SurfaceHolder.class));
		createCapturePreviewAndCallSurfaceChanged(mockInterface, mockWrapper);

		verify(mockInterface, times(1)).onCapturePreviewFailed();
	}

	public void test_shouldCallInterfaceWhenStartPreviewFails2() throws Exception {
		final CapturePreviewInterface mockInterface = mock(CapturePreviewInterface.class);
		final CameraWrapper mockWrapper = mock(CameraWrapper.class);
		doThrow(new RuntimeException()).when(mockWrapper).startPreview(any(SurfaceHolder.class));
		createCapturePreviewAndCallSurfaceChanged(mockInterface, mockWrapper);

		verify(mockInterface, times(1)).onCapturePreviewFailed();
	}

	public void test_shouldNotCallInterfaceWhenNoExceptions() throws Exception {
		final CapturePreviewInterface mockInterface = mock(CapturePreviewInterface.class);
		final CameraWrapper mockWrapper = mock(CameraWrapper.class);
		createCapturePreviewAndCallSurfaceChanged(mockInterface, mockWrapper);

		verify(mockInterface, never()).onCapturePreviewFailed();
	}

	private void createCapturePreviewAndCallSurfaceChanged(final CapturePreviewInterface mockInterface,
			final CameraWrapper mockWrapper) {
		final CapturePreview preview = new CapturePreview(mockInterface, mockWrapper, mock(SurfaceHolder.class), 0, 0);
		preview.surfaceChanged(null, 0, 0, 0);
	}
}
