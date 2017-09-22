package ingeniumbd.com.myapplication2;


public class Blog {
    public String title;
    public String audiofile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudiofile() {
        return audiofile;
    }

    public void setAudiofile(String audiofile) {
        this.audiofile = audiofile;
    }

    public Blog(String title, String audiofile) {
        this.title = title;
        this.audiofile = audiofile;

    }
    public Blog(){

    }
}
