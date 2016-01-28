package tetris;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

import tetris.Brain.Move;

public class JBrainTetris extends JTetris {

	Brain defaultBrain;
	private JCheckBox brainMode;
	private boolean useDefaultBrain;
	private Move goal;
	private boolean isGoalReached;
	private JSlider adversarySlider;
	private JLabel adversaryLabel;
	JBrainTetris(int pixels) {
		super(pixels);
		// TODO Auto-generated constructor stub
		this.defaultBrain = new DefaultBrain();
	}
	
	/**
	 Creates a frame with a JTetris.
	*/
	public static void main(String[] args) {
		// Set GUI Look And Feel Boilerplate.
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris brainTetris = new JBrainTetris(16);
		JFrame frame = JBrainTetris.createFrame(brainTetris);
		frame.setTitle("Tetris 2000");
		frame.setVisible(true);
	}
	

	
	/**
	 Creates the panel of UI controls -- controls wired
	 up to call methods on the JTetris. This code is very repetitive.
	*/
	public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();
        // brain panel
        JPanel brainPanel = new JPanel();
		brainMode = new JCheckBox("Brain");
		brainPanel.add(brainMode);
		panel.add(brainPanel);	
		// Adversary slider
		JPanel adversaryPanel = new JPanel();
		 panel.add(new JLabel("Adversary:"));
		adversarySlider = new JSlider(0, 100, 0);	// min, max, current
		adversarySlider.setPreferredSize(new Dimension(100, 15));
		adversaryPanel.add(adversarySlider);
		adversaryLabel = new JLabel();
		panel.add(adversaryPanel);
		panel.add(adversaryLabel);
		panel.remove(testButton);
		return panel;
	}
	
	public void startGame(){
		super.startGame();
		useDefaultBrain = brainMode.isSelected();		
	}
	
	public void tick(int verb) {
		if (!this.useDefaultBrain)
			super.tick(verb);
		else{
			if (!gameOn) return;
			
			if (currentPiece != null) {
				board.undo();	// remove the piece from its old position
			}
			
			// Sets the newXXX ivars
			computeNewPosition(verb);
			if (!isGoalReached)
				computeCompareGoal();
			
			// try out the new position (rolls back if it doesn't work)
			int result = setCurrent(newPiece, newX, newY);
			
			// if row clearing is going to happen, draw the
			// whole board so the green row shows up
			if (result ==  Board.PLACE_ROW_FILLED) {
				repaint();
			}
			

			boolean failed = (result >= Board.PLACE_OUT_BOUNDS);
			
			// if it didn't work, put it back the way it was
			if (failed) {
				if (currentPiece != null) board.place(currentPiece, currentX, currentY);
				repaintPiece(currentPiece, currentX, currentY);
			}
			
			/*
			 How to detect when a piece has landed:
			 if this move hits something on its DOWN verb,
			 and the previous verb was also DOWN (i.e. the player was not
			 still moving it),	then the previous position must be the correct
			 "landed" position, so we're done with the falling of this piece.
			*/
			if (failed && verb==DOWN && !moved) {	// it's landed
			
				int cleared = board.clearRows();
				if (cleared > 0) {
					// score goes up by 5, 10, 20, 40 for row clearing
					// clearing 4 gets you a beep!
					switch (cleared) {
						case 1: score += 5;	 break;
						case 2: score += 10;  break;
						case 3: score += 20;  break;
						case 4: score += 40; Toolkit.getDefaultToolkit().beep(); break;
						default: score += 50;  // could happen with non-standard pieces
					}
					updateCounters();
					repaint();	// repaint to show the result of the row clearing
				}
				
				
				// if the board is too tall, we've lost
				if (board.getMaxHeight() > board.getHeight() - TOP_SPACE) {
					stopGame();
				}
				// Otherwise add a new piece and keep playing
				else {
					addNewPiece();
					isGoalReached = false;
					goal = null;
				}
			}
			
			// Note if the player made a successful non-DOWN move --
			// used to detect if the piece has landed on the next tick()
			moved = (!failed && verb!=DOWN);
		}

	}
	
	/**
	 Updates the count/score labels with the latest values.
	 */
	private void updateCounters() {
		countLabel.setText("Pieces " + count);
		scoreLabel.setText("Score " + score);
	}
		
	private void computeCompareGoal(){
		if (goal == null) {
			board.undo();
			goal = this.defaultBrain.bestMove(board, newPiece,
					this.board.getHeight() - JTetris.TOP_SPACE, null);
			if (goal == null){
				return;
			}
		}
		//compare position with goal
		if (newX > goal.x)
			newX--;
		else if  (newX < goal.x)
			newX++;
		if (!newPiece.equals(goal.piece)){
			newPiece = newPiece.fastRotation();
		}
		// reached goal 
		if (newX == goal.x && newY == goal.y && newPiece.equals(goal.piece)){
			isGoalReached = true;
			goal = null;
		}		
	}
	
	/**
	 Selects the next piece to use using the random generator
	 set in startGame().
	*/
	public Piece pickNextPiece() {
		int value = this.adversarySlider.getValue();
		if (value == 0){
			 adversaryLabel.setVisible(false);
			 return super.pickNextPiece();
		}
		else{
			adversaryLabel.setVisible(true);
			int randomNo = random.nextInt(100);
			if (randomNo == 0){
				randomNo = random.nextInt(100);
			}
			if (randomNo >= value){
				adversaryLabel.setText("ok");
				return super.pickNextPiece();
			}			    
			else{
				double worstScore = 0;
				Piece worstPiece = null;
				// loop through the pieces 
				for (Piece piece : pieces){
				 Brain brain = new DefaultBrain();
					Move move = brain.bestMove(board, piece, this.board.getHeight()-4, null);
					if (move != null){
						double score = move.score;
						if (worstScore < score){
							worstScore = score;
							worstPiece = piece;
						}
					}					   
				}
				adversaryLabel.setText("*ok*");
				return worstPiece;
			}
			   
		}
	}
}
