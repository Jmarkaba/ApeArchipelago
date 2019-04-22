package GUIComponents;

import Entities.Entity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandBox extends BorderPane {

    public static final Background BLACK_BACKGROUND =
            new Background(new BackgroundFill(Color.BLACK, null, null));

    private PrimaryView main;
    private CommandList commandList;
    private Inventory inventory;

    // For possible commands
    public enum Command {
        OPEN, LOOK, WALK, TALK, PUSH,
        CLOSE, GRAB, USE, DROP
    }

    // Mapping commands to their corresponding string values
    private HashMap<String, Command> textToCommand = new HashMap();
    private Command currentCommand;
    private String currentCommandText;
    private Label currentCommandLabel;


    // Constructor
    public CommandBox(PrimaryView main) {
        this.main = main;
        super.setBackground(BLACK_BACKGROUND);
        super.setPadding(new Insets(0, 20, 10,20));

        putCommandsInTable();

        this.commandList = new CommandList();
        this.inventory = new Inventory();
        this.currentCommandLabel = new Label();
        this.currentCommandLabel.setTextFill(Color.PURPLE);
        this.currentCommandLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 18));
        clearCommand();
        BorderPane.setAlignment(currentCommandLabel, Pos.CENTER);
        super.setTop(currentCommandLabel);
        super.setCenter(commandList);
        super.setRight(inventory);
    }

    private void putCommandsInTable() {
        textToCommand.put("Use ", Command.USE);
        textToCommand.put("Open ", Command.OPEN);
        textToCommand.put("Close ", Command.CLOSE);
        textToCommand.put("Drop ", Command.DROP);
        textToCommand.put("Push ", Command.PUSH);
        textToCommand.put("Pick up ", Command.GRAB);
        textToCommand.put("Look at ", Command.LOOK);
        textToCommand.put("Walk to ", Command.WALK);
        textToCommand.put("Talk to ", Command.TALK);
    }

    public void setDimensions(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
    }

    public void hide() {
        commandList.hide();
        inventory.hide();
    }

    public void showDefault() {
        commandList.show();
        inventory.show();
    }

    public void update() {
        if(!inventory.balanced()) {
            inventory.update();
        }
    }



    /*
    Commands and command label code
     */
    public Command getCommand() {
        return currentCommand;
    }
    public Label getCommandLabel() { return currentCommandLabel; }

    public void clearCommand() {
        currentCommand = Command.WALK;
        currentCommandText = "Walk to ";
        currentCommandLabel.setText(currentCommandText);
    }

    public void setCommandLabelText(String name) {
        String forLabel = currentCommandText + name;
        currentCommandLabel.setText(forLabel);
    }

    private class CommandList extends GridPane {
        public CommandList() {
            show();
        }

        public void hide() {
            super.getChildren().clear();
        }

        public void show() {
            int row = 0;
            int col = 0;
            for(String key : textToCommand.keySet()) {
                CommandLabel cl = new CommandLabel(key);
                super.add(cl, col, row);
                col++;
                if(col % 3 == 0) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private class CommandLabel extends Label {

        public CommandLabel(String text) {
            super(text);
            super.setPadding(new Insets(5));
            super.setTextFill(Color.YELLOW);
            super.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
            this.setOnMouseClicked((event -> {
                currentCommandText = super.getText();
                currentCommand = textToCommand.get(currentCommandText);
                currentCommandLabel.setText(currentCommandText);
            }
            ));

            this.setOnMouseEntered((event -> setCursor(Cursor.HAND)));
            this.setOnMouseExited((event -> setCursor(Cursor.DEFAULT)));
        }
    }



    /*
    Inventory based code
     */
    public void addItem(String itemName, Image image, Entity e) {
        inventory.addItem(image, itemName, e);
    }

    public boolean hasItem(String itemName) {
        for(InventoryItem i : inventory.items) {
            if(i.getName().equals(itemName))
                return true;
        }
        return false;
    }

    private class Inventory extends GridPane {

        private ArrayList<InventoryItem> items;

        public Inventory() {
            super.setPadding(new Insets(0,20,0,20));
            super.setHgap(15);
            super.setVgap(10);
            items = new ArrayList<>();
        }

        public void addItem(Image image, String name, Entity e) {
            InventoryItem item = new InventoryItem(this, image, name, e);
            items.add(item);
            super.getChildren().add(item);
            update();
        }

        public void remove(String id) {
            for(InventoryItem i : items) {
                if(i.getName().equals(id)) {
                    items.remove(i);
                    break;
                }
            }
            for(Node item : this.getChildren()) {
                if(((InventoryItem) item).getName().equals(id))
                    super.getChildren().remove(item);
            }
            update();
        }

        public void update() {
            hide();
            show();
        }

        public boolean balanced() {
            return items.size() == this.getChildren().size();
        }

        public void hide() {
            super.getChildren().clear();
        }

        public void show() {
            int row = 0, col = 0;
            for(InventoryItem item : items) {
                super.add(item, col, row);
                col++;
                if(col % 4 == 0) {
                    col=0;
                    row++;
                }
            }
        }
    }

    private class InventoryItem extends ImageView {

        private String id;
        private Entity ent;

        public InventoryItem(Inventory parent, Image i, String name, Entity ent) {
            super(new Image(i.getUrl(),35,35,false,false));
            this.id = name;
            this.ent = ent;

            this.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->  {
                getScene().setCursor(Cursor.CROSSHAIR);
                if(getCommand() != null) {
                    setCommandLabelText(id);
                }
                event.consume();
            });
            this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                getScene().setCursor(Cursor.DEFAULT);
                if(getCommand() != null) {
                    setCommandLabelText("");
                }
                event.consume();
            });
            this.setOnMouseClicked(event  ->  {
                if(currentCommand == Command.DROP) {
                    try {
                        ent.setPosition(400, 200);
                        main.getEntityView().addEntity(ent);
                        parent.remove(this.id);
                    } catch (Exception e) { }
                }
            });
        }

        public String getName() {
            return id;
        }
    }
}
