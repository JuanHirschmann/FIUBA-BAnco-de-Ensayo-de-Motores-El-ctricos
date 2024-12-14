import Controller.Controller;
import Model.Model;
import Views.Views;

public class App {
 
    public static void main(String[] args) {
        Model model = new Model();
        Views view = new Views();
        Controller controller = new Controller(model, view);
        view.setController(controller);
    }

}

