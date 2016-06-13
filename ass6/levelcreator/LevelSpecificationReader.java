package levelcreator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import backgrounds.ColorBackground;
import backgrounds.ImgeBackground;
import gameobjects.Block;
import gameobjects.Sprite;
import gameobjects.Velocity;
import levels.LevelInformation;

public class LevelSpecificationReader{

    private int startX;
    private int xPosition;
    private int yPosition;
    private int rowHeight;
    private boolean readBlockLines;

    public List<LevelInformation> fromReader(java.io.Reader reader) throws FileNotFoundException {
        List<LevelInformation> levels = new ArrayList<LevelInformation>();
        Level level = null;
        BlocksFromSymbolsFactory factory = null;
        BufferedReader bufferReader = new BufferedReader(reader);
        try {
            String line = null;
            while ((line = bufferReader.readLine ()) != null ) {
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if ("START_LEVEL".equals(line)) {
                    level = new Level();
                    continue;
                }
                if ("END_LEVEL".equals(line)) {
                    levels.add(level);
                    xPosition = 0;
                    yPosition = 0;
                    rowHeight = 0;
                    factory = null;
                    continue;
                }
                String[] parts = line.split(":");
                String key = parts[0];
                if(key.equals("START_BLOCKS")){
                    readBlockLines = true;
                    continue;
                }
                if(key.equals("END_BLOCKS")){
                    readBlockLines = false;
                    continue;
                }
                if (readBlockLines){
                    for (int i = 0; i < line.length(); ++i) {
                        String symbol = Character.toString(line.charAt(i));
                        if(factory.isSpaceSymbol(symbol)){
                            xPosition += factory.getSpaceWidth(symbol);
                        }else if(factory.isBlockSymbol(symbol)){
                            //System.out.println(symbol);
                            Block block = factory.getBlock(symbol, xPosition, yPosition);
                            level.blockList.add(block);
                            xPosition += factory.getBlockWidth(symbol);
                        }
                    }
                    yPosition += rowHeight;
                    xPosition = startX;
                    continue;
                }
                String value = parts[1];
                if(key.equals("level_name")){
                    level.levelName = (value);
                    continue;
                }
                if(key.equals("ball_velocities")){
                    String[] velocities = value.split(" ");
                    for(int i = 0; i < velocities.length; i++){
                        String[] velocity = velocities[i].split(",");
                        level.velocities.add(Velocity.fromAngleAndSpeed(Double.parseDouble(velocity[0]), (Double.parseDouble(velocity[1]))));
                    }
                    level.numberOfBalls = level.velocities.size();
                    continue;
                }
                if(key.equals("background")){
                    ColorsParser colorParser = new ColorsParser();
                    if(value.startsWith("color")){
                        Color color = colorParser.colorFromString(value);
                        level.background = new ColorBackground(color);
                        continue;
                    } else if(value.startsWith("image")){
                        String imageLine = value.substring(6, value.length() -1);
                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(new File(imageLine));
                            level.background = new ImgeBackground(image);
                            continue;
                        } catch (IOException e) {

                        }
                    }
                }
                if(key.equals("paddle_speed")){
                    level.paddleSpeed = Integer.parseInt(value);
                }
                if(key.equals("paddle_width")){
                    level.paddleWidth = Integer.parseInt(value);
                }
                if(key.equals("block_definitions")){
                    factory = BlockDefinitionsReader.fromReader(reader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(value))));
                    continue;
                }

                if(key.equals("blocks_start_x")){
                    this.xPosition = Integer.parseInt(value);
                    startX = xPosition;
                    continue;
                }

                if(key.equals("blocks_start_y")){
                    this.yPosition = Integer.parseInt(value);
                    continue;
                }

                if(key.equals("row_height")){
                    this.rowHeight = Integer.parseInt(value);
                    continue;
                }

                if(key.equals("num_blocks")){
                    level.numberOfBlocksToRemove = Integer.parseInt(value);
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find level specs file ");
            throw e;
        } catch (IOException e) {
            System.err.println("Failed reading level specs file " + ", message:" + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Failed closing file");
            }
        }
        return levels;
    }

    private class Level implements LevelInformation {

        private List<Velocity> velocities = new ArrayList<Velocity>();
        private List<Block> blockList = new ArrayList<Block>();
        private int numberOfBlocksToRemove;
        private int paddleSpeed;
        private int paddleWidth;
        private String levelName;
        private int numberOfBalls;
        private Sprite background;

        @Override
        public int numberOfBalls() {
            return this.numberOfBalls;
        }
        @Override
        public List<Velocity> initialBallVelocities() {
            return this.velocities;
        }
        @Override
        public int paddleSpeed() {
            return this.paddleSpeed;
        }
        @Override
        public int paddleWidth() {
            return this.paddleWidth;
        }
        @Override
        public String levelName() {
            return this.levelName;
        }
        @Override
        public Sprite getBackground() {
            return this.background;
        }
        @Override
        public List<Block> blocks() {
            return blockList;
        }

        @Override
        public int numberOfBlocksToRemove() {
            return this.numberOfBlocksToRemove;
        }
    }
}