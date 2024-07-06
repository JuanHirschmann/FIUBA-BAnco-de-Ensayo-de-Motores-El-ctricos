import Model.Model;
import Views.Views;

public class App {
    
    public static void main(String[] args) {
        
        Views view=new Views();
        view.launchMainWindow();
        Model model=new Model();
        //System.err.println(model);
        String url ="batata";
        try {
            model.connect(url);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
    }
    public static void setUp()
    {

    }
}
