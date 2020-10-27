package com.github.theangrybeavers.ydbot.service.mediaService;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import java.io.File;

@Service("VideoToAudioConverter")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VideoToAudioConverter {
	private final static int BIT_RATE = 128_000;
	private final static int AUDIO_CHANNELS = 2;
	private final static int SAMPLE_BIT_RATE = 44_100;
	private final static String AUDIO_CODEC = "libmp3lame";

	public void convert(String inputVideoFile, String outputAudioFile) throws EncoderException {
		Encoder mediaEncoder = new Encoder();

		AudioAttributes audioSettings = new AudioAttributes();
		audioSettings.setBitRate(BIT_RATE);
		audioSettings.setChannels(AUDIO_CHANNELS);
		audioSettings.setSamplingRate(SAMPLE_BIT_RATE);
		audioSettings.setCodec(AUDIO_CODEC);

		EncodingAttributes encodingSettings = new EncodingAttributes();
		encodingSettings.setFormat("mp3");
		encodingSettings.setAudioAttributes(audioSettings);

		File videoInFile = new File(inputVideoFile);
		File audioOutFile = new File(outputAudioFile);

		mediaEncoder.encode(new MultimediaObject(videoInFile), audioOutFile, encodingSettings);
	}

}
