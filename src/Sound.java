import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class Sound extends Thread {
    Sound(){
        play();
    }

    public void play() {
        try{
            URL url = getClass().getResource("resource/marsch.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(url.getPath()));
            Clip test = AudioSystem.getClip();

            test.open(ais);
            test.start();

            while (!test.isRunning())
                Thread.sleep(10);
            while (test.isRunning())
                Thread.sleep(10);

            test.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}