class Sudoku {
    public static final int GRID_SIZE = 9;
    public static final int BOX_SIZE = 3;
    private int[][] grid;

    public Sudoku(String inputStr) {
        grid = new int[GRID_SIZE][GRID_SIZE];
        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = inputStr.charAt(index) - '0';
                index++;
            }
        }
    }

    public Sudoku clone() {
        Sudoku clonedSudoku = new Sudoku("0".repeat(GRID_SIZE * GRID_SIZE));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                clonedSudoku.grid[row][col] = grid[row][col];
            }
        }
        return clonedSudoku;
    }
    //串行化
    public String serialize() {
        StringBuilder serializedStr = new StringBuilder();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                serializedStr.append(grid[row][col]);
            }
        }
        return serializedStr.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Sudoku)) {
            return false;
        }
        Sudoku otherSudoku = (Sudoku) other;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col]!= otherSudoku.grid[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] getRow(int rowIndex) {
        return grid[rowIndex];
    }

    public int[] getColumn(int colIndex) {
        int[] column = new int[GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            column[row] = grid[row][colIndex];
        }
        return column;
    }

    public int[] getBox(int row, int col) {
        int[] box = new int[BOX_SIZE * BOX_SIZE];
        int boxRowStart = BOX_SIZE * (row / BOX_SIZE);
        int boxColStart = BOX_SIZE * (col / BOX_SIZE);
        int index = 0;
        for (int i = boxRowStart; i < boxRowStart + BOX_SIZE; i++) {
            for (int j = boxColStart; j < boxColStart + BOX_SIZE; j++) {
                box[index++] = grid[i][j];
            }
        }
        return box;
    }
    //判断（row、col）格是否能填入num
    public boolean is_valid_move(int row, int col, int num) {
        // 检查行
        for (int i = 0; i < GRID_SIZE; i++) {
            if (grid[row][i] == num) {
                return false;
            }
        }
        // 检查列
        for (int i = 0; i < GRID_SIZE; i++) {
            if (grid[i][col] == num) {
                return false;
            }
        }
        // 检查 3x3 子格
        int boxRowStart = BOX_SIZE * (row / BOX_SIZE);
        int boxColStart = BOX_SIZE * (col / BOX_SIZE);
        for (int i = boxRowStart; i < boxRowStart + BOX_SIZE; i++) {
            for (int j = boxColStart; j < boxColStart + BOX_SIZE; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
    //使用回溯方法尝试解决该数独问题
    public boolean solve() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= GRID_SIZE; num++) {
                        if (is_valid_move(row, col, num)) {
                            grid[row][col] = num;
                            if (solve()) {
                                return true;
                            } else {
                                grid[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    //判断数独问题是否被解决
    public boolean is_solved() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    return false;
                }
                if (!is_valid_move(row, col, grid[row][col])) {
                    return false;
                }
            }
        }
        return true;
    }
    //打印数独表格
    public void print_grid() {
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
    //统计还剩多少未填格子
    public int count_empty_cells() {
        int count = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    count++;
                }
            }
        }
        return count;
    }
}

class SudokuTest {
    public static void main(String[] args) {
        // 测试构造函数和序列化
        String inputStr = "017903600000080000900000507072010430000402070064370250701000065000030000005601720";
        Sudoku sudoku = new Sudoku(inputStr);
        String serializedStr = sudoku.serialize();
        assert serializedStr.equals(inputStr) : "序列化错误";

        // 测试克隆
        Sudoku clonedSudoku = sudoku.clone();
        assert sudoku.equals(clonedSudoku) : "克隆错误";

        // 测试获取行、列和子格
        int[] row = sudoku.getRow(0);
        assert row.length == Sudoku.GRID_SIZE : "获取行错误";
        int[] column = sudoku.getColumn(0);
        assert column.length == Sudoku.GRID_SIZE : "获取列错误";
        int[] box = sudoku.getBox(0, 0);
        assert box.length == Sudoku.BOX_SIZE * Sudoku.BOX_SIZE : "获取子格错误";

        // 测试有效性检查
        assert sudoku.is_valid_move(0, 0, 1) : "有效性检查错误";
        assert!sudoku.is_valid_move(0, 0, row[0]) : "有效性检查错误";

        // 测试求解和是否已解决
        boolean solvedBefore = sudoku.is_solved();
        assert!solvedBefore : "初始状态错误地被认为已解决";
        sudoku.solve();
        boolean solvedAfter = sudoku.is_solved();
        assert solvedAfter : "求解错误";

        // 测试打印
        System.out.println("原始数独：");
        new Sudoku(inputStr).print_grid();
        System.out.println("求解后的数独：");
        sudoku.print_grid();

        // 测试统计空白单元格
        int emptyCellsBefore = new Sudoku(inputStr).count_empty_cells();
        assert emptyCellsBefore > 0 : "空白单元格统计错误";
        int emptyCellsAfter = sudoku.count_empty_cells();
        assert emptyCellsAfter == 0 : "空白单元格统计错误";
    }
}
