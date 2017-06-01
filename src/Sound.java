import java.io.File;
import javax.sound.sampled.*;

public class Sound extends Thread {
    Sound(){
        play();
    }

    public static void play() {
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("C:\\Users\\Trist\\Documents\\Veldslag\\src\\resource\\marsch.wav"));
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