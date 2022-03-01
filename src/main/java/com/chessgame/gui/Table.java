package com.chessgame.gui;


import com.chessgame.engine.GameManager;
import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.move.Move;
import com.chessgame.engine.move.PromotionPrompt;
import com.chessgame.engine.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {

    private final JFrame gameFrame;
    private BoardPanel boardPanel;
    private final GameManager gameManager;

    private Optional<Piece> selectedPiece;
    private Collection<Move> highlightedMoves;
    private boolean optionHighlight;
    private boolean optionAi;
    private boolean optionFlipBoard;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final static Color LIGHTER_COLOR = new Color(227, 193, 111);
    private final static Color DARKER_COLOR = new Color(184, 139, 74);
    private final static Color LIGHTER_COLOR_GREEN = new Color(217, 227, 111);
    private final static Color DARKER_COLOR_GREEN = new Color(184, 175, 74);
    private final static Color LIGHTER_COLOR_RED = new Color(227, 134, 111);
    private final static Color DARKER_COLOR_RED = new Color(184, 100, 74);
    private final static String STANDARD_PIECES_PATH = "src/resource/";


    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameManager = new GameManager(generatePromotionPrompt());
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        highlightedMoves = new LinkedList<>();
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectedPiece = Optional.empty();
        highlightedMoves.clear();
        optionHighlight = true;
        optionAi = true;
        optionFlipBoard = false;
    }

    private PromotionPrompt generatePromotionPrompt() {
        return () -> JOptionPane.showInputDialog(this.gameFrame,
                  "Choose a piece to promote your pawn into:\n[Q]ueen\n[R]ook\n[B]ishop\n[K]night");
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createGameMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createPreferencesMenu() {
        final JMenu gameMenu = new JMenu("Preferences");

        final JCheckBox highlightCheckbox = new JCheckBox("Highlight Tiles");
        highlightCheckbox.setSelected(true);
        highlightCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionHighlight = !optionHighlight;
            }
        });
        gameMenu.add(highlightCheckbox);

        final JCheckBox aiCheckbox = new JCheckBox("AI");
        aiCheckbox.setSelected(true);
        aiCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionAi = !optionAi;
            }
        });
        gameMenu.add(aiCheckbox);

        final JCheckBox flipBoardCheckbox = new JCheckBox("Flip Board");
        flipBoardCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionFlipBoard = !optionFlipBoard;
                boardPanel.flipBoard();
            }
        });
        gameMenu.add(flipBoardCheckbox);

        return gameMenu;
    }

    private JMenu createGameMenu() {
        final JMenu gameMenu = new JMenu("Game");

        final JMenuItem restart = new JMenuItem("New Game");
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.restartGame();
                boardPanel.updateTiles();
            }
        });
        gameMenu.add(restart);

        final JMenuItem open = new JMenuItem("Load Game");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gameFrame, "Loading games not supported yet!");
                //TODO
            }
        });
        gameMenu.add(open);

        return gameMenu;
    }


    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for(int y = 7; y >= 0; y--) {
                for(int x = 0; x <8; x++) {
                    final TilePanel tilePanel = new TilePanel(this, new Point(x,y));
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void updateTiles() {
            for(var tile : boardTiles) {
                    tile.removeAll();
                    tile.assignTileColor();
                    tile.highlightLegals();
                    tile.assignTilePiece(gameManager.getCurrentBoard());
                    tile.repaint();
                    tile.validate();
            }
        }

        public void flipBoard() {
            removeAll();
            Collections.reverse(this.boardTiles);
            boardTiles.forEach(t -> add(t));
            updateTiles();
        }

    }

    private class TilePanel extends JPanel {
        private final Point tilePoint;
        private final BoardPanel boardPanel;

        TilePanel(final BoardPanel boardPanel, Point point) {
            super(new GridBagLayout());
            this.tilePoint = point;
            this.boardPanel = boardPanel;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTilePiece(gameManager.getCurrentBoard());
            assignTileColor();
            validate();

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        selectedPiece = Optional.empty();
                        highlightedMoves.clear();
                    } else if (isLeftMouseButton(e)) {
                        if (selectedPiece.filter(p -> gameManager.getCurrentBoard()
                                        .getAllianceTurn() == p.getAlliance()).isPresent()){
                            Optional<Move> moveOptional = highlightedMoves.stream()
                                        .filter(m -> m.getDestination().equals(tilePoint)).findAny();
                            if (moveOptional.isPresent()) {
                                gameManager.applyMove(moveOptional.get());
                                highlightedMoves.clear();
                                if(optionAi) {
                                    gameManager.runRandomTurn();
                                }
                            } else {
                                if (gameManager.getCurrentBoard().getPiece(tilePoint)
                                       .filter(p -> gameManager.getCurrentBoard().getAllianceTurn() == p.getAlliance())
                                       .isPresent()) {
                                   selectedPiece = gameManager.getCurrentBoard().getPiece(tilePoint);
                                   highlightedMoves = gameManager.getMovesFromPiece(tilePoint);
                                } else {
                                    selectedPiece = Optional.empty();
                                }
                            }
                        } else {
                            selectedPiece = gameManager.getCurrentBoard().getPiece(tilePoint);
                            highlightedMoves = gameManager.getMovesFromPiece(tilePoint);
                        }
                    }
                    boardPanel.updateTiles();
                }



                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
        }

        public void setColor(Color color) {
            setBackground(color);
        }

        public void highlightLegals() {
            if(optionHighlight) {
                for (Move move : highlightedMoves) {
                    if (move.getDestination().equals(this.tilePoint)) {
                        this.setColor((this.tilePoint.x+this.tilePoint.y)%2 ==0 ? DARKER_COLOR_GREEN : LIGHTER_COLOR_GREEN);
                    /*final BufferedImage Image = ImageIO.read(new File(STANDARD_PIECES_PATH + "dot.png"));
                    add(new JLabel(new ImageIcon(Image)));*/
                        if(gameManager.getCurrentBoard().getPiece(tilePoint).isPresent()) {
                            this.setColor((this.tilePoint.x+this.tilePoint.y)%2 ==0 ? DARKER_COLOR_RED : LIGHTER_COLOR_RED);
                        }
                    }
                }
            }

        }
        public void assignTileColor() {
            this.setColor((this.tilePoint.x+this.tilePoint.y)%2 ==0 ?  DARKER_COLOR : LIGHTER_COLOR);
        }

        public void assignTilePiece(Board board) {
            var pieceOptional = board.getPiece(tilePoint);
            if (pieceOptional.isPresent()){
                Piece piece = pieceOptional.get();
                try {
                    final BufferedImage Image = ImageIO.read(new File(STANDARD_PIECES_PATH + piece + ".png"));
                    add(new JLabel(new ImageIcon(Image)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                return;
            }


        }

    }
}
