package multi_agent_painting.mas.sound;


import java.io.IOException;

/**
 * Created by Epulapp on 16/01/2017.
 */
public interface Listenable {

    public void initSound();

    public void listenToSample() throws IOException;
}
