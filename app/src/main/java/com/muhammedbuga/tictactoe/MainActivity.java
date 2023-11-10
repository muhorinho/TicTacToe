package com.muhammedbuga.tictactoe;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_X = "X";
    static final String PLAYER_O = "O";
    boolean isPlayerXTurn = true;
    byte[][] board = new byte[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeBoard();
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onButtonClick((Button) view);
        }
    };

    private void initializeBoard() {
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Clear existing views in GridLayout
        gridLayout.removeAllViews();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams());
                button.setOnClickListener(buttonClickListener);
                button.setTag(R.string.row, i);
                button.setTag(R.string.col, j);
                gridLayout.addView(button);
            }
        }

    }

    private void onButtonClick(Button button) {
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) button.getLayoutParams();

        int row = Integer.parseInt(button.getTag(R.string.row).toString());
        int col = Integer.parseInt(button.getTag(R.string.col).toString());

        if (isValidMove(row, col)) {
            makeMove(button, row, col);
            if (checkForWinner()) {
                showGameOverMessage("Player " + (isPlayerXTurn ? PLAYER_X : PLAYER_O) + " wins!");
            } else if (isBoardFull()) {
                showGameOverMessage("It's a draw!");
            } else {
                isPlayerXTurn = !isPlayerXTurn;
            }
        } else {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidMove(int row, int col) {
        return board[row][col] == 0;
    }

    private void makeMove(Button button, int row, int col) {
        if (isPlayerXTurn) {
            board[row][col] = 1;
            button.setText(PLAYER_X);
        } else {
            board[row][col] = 2;
            button.setText(PLAYER_O);
        }
        button.setEnabled(false);
    }

    private boolean checkForWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true; // Row win
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return true; // Column win
            }
        }

        // Check diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return true; // Diagonal win (top-left to bottom-right)
        }

        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return true; // Diagonal win (top-right to bottom-left)
        }

        return false;
    }

    private boolean isBoardFull() {
        // Check if the board is full
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false; // There is an empty cell, the board is not full
                }
            }
        }
        return true; // All cells are filled, the board is full
    }


    private void showGameOverMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        resetGame();
    }

    private void resetGame() {
        initializeBoard();
        isPlayerXTurn = true;
        resetBoardState();

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                ((Button) child).setText("");
                ((Button) child).setEnabled(true);
            }
        }
    }
    private void resetBoardState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_restart) {
            restartGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartGame() {
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Clear existing views in GridLayout
        gridLayout.removeAllViews();

        // Initialize the board and buttons
        initializeBoard();

        // Reset the game state
        isPlayerXTurn = true;
        resetBoardState();

        // Enable all buttons
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                ((Button) child).setEnabled(true);
            }
        }
    }
}
