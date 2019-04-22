package GUIComponents;

import Entities.DecorativeEntity;
import Entities.Entity;
import Entities.InteractableEntity;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EntityView{

    private static final String ENTITY_FILE = "entities.txt";
    private static final String DETAILS_FILE = "details.txt";
    private static final String CYCLES_FILE = "cycles.txt";

    private Entity[] entities;
    private int numEntities;
    private String directory;

    private PrimaryView main;

    public EntityView(PrimaryView main, String sceneDirectory) throws IOException {
        clear();
        this.main = main;
        this.directory = sceneDirectory + "/" + "entities/";

        File fileToForm = new File(getFile(this.directory+ "/" +ENTITY_FILE));
        FileReader fr = new FileReader(fileToForm);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null) {
            addEntity(line.trim(), this.directory);
        }
    }

    public void addEntity(Entity e) {
        System.out.println(e);
        if(numEntities < 51) {
            entities[numEntities] = e;
            numEntities++;
            for(int i = 0; i < numEntities; ++i) {
                System.out.println(entities[i]);
            }
        } else throw new IndexOutOfBoundsException();
    }

    public void addEntity(String entityName, String directory) throws IOException{
        if(numEntities < 51) {
            entities[numEntities] = generateEntity(entityName, directory);
            numEntities++;
        } else throw new IndexOutOfBoundsException();
    }

    public Entity generateEntity(String entityName, String directory) throws IOException {
        String entDir = directory + entityName;
        Entity e;
        /*
         * Load the details of the entity
         */
        File detailsFile = new File(getFile(entDir + "/" + DETAILS_FILE));
        FileReader fr = new FileReader(detailsFile);
        BufferedReader br = new BufferedReader(fr);

        //Create img
        String imgUrl = entDir + "/" + entityName + ".png";
        double width = Double.parseDouble(br.readLine());
        double height = Double.parseDouble(br.readLine());
        Image i = new Image(imgUrl, width, height, false, false);
        //Upload to entity
        if (br.readLine().trim().equals("true"))
            e = new InteractableEntity(this.main, i, entityName, Double.parseDouble(br.readLine()), Double.parseDouble(br.readLine()));
        else
            e = new DecorativeEntity(i, entityName, Double.parseDouble(br.readLine()), Double.parseDouble(br.readLine()));
        e.setIsBackground(br.readLine().trim().equals("true"));
        e.setDimensions(width, height);
        //e.setVelocity(1,-2);
        /*
         * Add cycles if any
         */
        File cyclesFile = new File(getFile(entDir + "/" + CYCLES_FILE));
        fr = new FileReader(cyclesFile);
        br = new BufferedReader(fr);
        String line;
        while((line = br.readLine()) != null) {
            String[] all = line.trim().split(",");
            String[] data = new String[all.length-1];
            for(int j = 0; j < data.length; ++j) {
                data[j] = entDir+ "/cycles/" +all[j+1];
            }
            e.addCycle(all[0], data);
        }
        return e;
    }

    public void renderAll() {
        for(int i = 0; i < numEntities; ++i) {
            entities[i].render(this.main);
        }
    }

    public void updateAll(double time) {
        for(int i = 0; i < numEntities; ++i) {
            entities[i].update(time);
        }
    }

    public Entity getEntity(String name) {
        for(int i = 0; i < numEntities; ++i) {
            if(entities[i].getName().equals(name)) {
                return entities[i];
            }
        }
        return null;
    }

    public void removeEntity(String name) {
        for(int i = 0; i < numEntities; ++i) {
            if(entities[i].getName().equals(name)) {
                main.getGamePane().getPlayerPane(entities[i].isBackground).getChildren().remove(((InteractableEntity)entities[i]).getImageView());
                for(int j = i; j < numEntities; ++j) {
                    entities[j] = entities[j+1];
                    numEntities--;
                    break;
                }
            }
        }
    }

    public void clear() {
        entities = new Entity[51];
        numEntities = 0;
    }

    public String getFile(String resource) {
        return getClass().getClassLoader().getResource(resource).getFile();
    }
}
