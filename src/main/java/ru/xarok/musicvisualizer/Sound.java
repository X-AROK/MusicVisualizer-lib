package ru.xarok.musicvisualizer;

import com.badlogic.audio.io.MP3Decoder;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.jcodec.api.SequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.common.model.Rational;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.jcodec.common.Codec.H264;
import static org.jcodec.common.Format.MOV;

public class Sound{
    private Path path;
    private int duration;
    private int framesCount;

    public Sound(Path path){
        this.path = path;
        duration = getDuration();
        framesCount = getFramesCount();
    }

    private int getDuration(){
        try {
            File file = new File(path.toUri());
            Encoder encoder = new Encoder();
            MultimediaInfo mi = encoder.getInfo(file);
            return (int) (mi.getDuration() / 1000);
        }
        catch (Exception ex){
            System.err.println(ex.getMessage());
            return 0;
        }
    }

    private int getFramesCount(){
        try {
            float[] samples = new float[1024];
            MP3Decoder decoder = new MP3Decoder(Files.newInputStream(path));
            int allCount = 0;
            int framesCount = 0;
            while (decoder.readSamples(samples) > 0) {
                allCount++;
                if (allCount % 2 == 1) continue;
                framesCount++;
            }
            return framesCount;
        }
        catch (Exception ex){
            System.err.println(ex.getMessage());
            return 0;
        }
    }

    public void generateMP4(File out, Visualizer visualizer, ProgressListener listener){
        if(!out.getPath().endsWith(".mp4")){
            System.err.println("E\\Sound: Invalid out file format.");
            return;
        }
        if(duration == 0 || framesCount == 0){
            System.err.println("E\\Sound: Error on sound init.");
            return;
        }
        try {

            if (out.exists() || out.createNewFile()) {
                MP3Decoder decoder = new MP3Decoder(Files.newInputStream(path));
                SequenceEncoder enc = new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(framesCount, duration), MOV, H264, null);

                float[] samples = new float[1024];
                int currentFrame = 0;
                int iEncodedFrame = 0;
                while (decoder.readSamples(samples) > 0) {
                    currentFrame++;
                    if(currentFrame % 2 == 1) continue; //Encoding every 2nd frame; 43 fps is too fast

                    visualizer.setSamples(samples);

                    BufferedImage image = visualizer.getImage();
                    Picture pic = Picture.create(image.getWidth(), image.getHeight(), ColorSpace.RGB);
                    AWTUtil.fromBufferedImage(image, pic);

                    enc.encodeNativeFrame(pic);

                    if(listener != null) listener.onProgressChange(++iEncodedFrame, framesCount);
                }
                enc.finish();
                if(listener != null) listener.onComplete();
            }
        }
        catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}
