class Sudoku:
    GRID_SIZE = 9
    BOX_SIZE = 3

    def __init__(self, input_str):
        """
        对象创建和初始化。
        将输入字符串解析为九宫格的初始状态。
        """
        self.grid = [[0] * Sudoku.GRID_SIZE for _ in range(Sudoku.GRID_SIZE)]
        index = 0
        for row in range(Sudoku.GRID_SIZE):
            for col in range(Sudoku.GRID_SIZE):
                self.grid[row][col] = int(input_str[index])
                index += 1

    def clone(self):
        """
        克隆方法，创建当前九宫格的副本。
        """
        cloned_grid = Sudoku('0' * Sudoku.GRID_SIZE * Sudoku.GRID_SIZE)
        for row in range(Sudoku.GRID_SIZE):
            for col in range(Sudoku.GRID_SIZE):
                cloned_grid.grid[row][col] = self.grid[row][col]
        return cloned_grid

    def serialize(self):
        """
        串行化方法，将九宫格状态转换为字符串。
        """
        serialized_str = ''
        for row in range(Sudoku.GRID_SIZE):
            for col in range(Sudoku.GRID_SIZE):
                serialized_str += str(self.grid[row][col])
        return serialized_str

    def __eq__(self, other):
        """
        比较方法，判断两个九宫格是否相等。
        """
        if not isinstance(other, Sudoku):
            return False
        for row in range(Sudoku.GRID_SIZE):
            for col in range(Sudoku.GRID_SIZE):
                if self.grid[row][col]!= other.grid[row][col]:
                    return False
        return True

    def get_candidates(self, row, col):
        """
        获取指定单元格的候选值。
        """
        candidates = set(range(1, Sudoku.GRID_SIZE + 1))
        # 检查所在行
        for i in range(Sudoku.GRID_SIZE):
            if self.grid[row][i] in candidates:
                candidates.remove(self.grid[row][i])
        # 检查所在列
        for i in range(Sudoku.GRID_SIZE):
            if self.grid[i][col] in candidates:
                candidates.remove(self.grid[i][col])
        # 检查所在 3x3 子格
        start_row, start_col = Sudoku.BOX_SIZE * (row // Sudoku.BOX_SIZE), Sudoku.BOX_SIZE * (col // Sudoku.BOX_SIZE)
        for i in range(start_row, start_row + Sudoku.BOX_SIZE):
            for j in range(start_col, start_col + Sudoku.BOX_SIZE):
                if self.grid[i][j] in candidates:
                    candidates.remove(self.grid[i][j])
        return candidates

    def getRow(self, row_index):
        """
        获取指定行。
        """
        return self.grid[row_index]

    def getColumn(self, col_index):
        """
        获取指定列。
        """
        return [self.grid[i][col_index] for i in range(Sudoku.GRID_SIZE)]

    def getBox(self, row, col):
        """
        获取指定行和列所在的 3x3 子格。
        """
        box_row_start = Sudoku.BOX_SIZE * (row // Sudoku.BOX_SIZE)
        box_col_start = Sudoku.BOX_SIZE * (col // Sudoku.BOX_SIZE)
        box = []
        for i in range(box_row_start, box_row_start + Sudoku.BOX_SIZE):
            for j in range(box_col_start, box_col_start + Sudoku.BOX_SIZE):
                box.append(self.grid[i][j])
        return box

    def solve(self):
        """
        使用回溯算法尝试求解数独
        """
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] == 0:
                    for num in range(1, self.GRID_SIZE + 1):
                        if self.is_valid_move(row, col, num):
                            self.grid[row][col] = num
                            if self.solve():
                                return True
                            else:
                                self.grid[row][col] = 0
                    return False
        return True

    def is_solved(self):
        """
        判断数独问题是否求解成功
        """
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] == 0:
                    return False
                if not self.is_valid_move(row, col, self.grid[row][col]):
                    return False
        return True

    def print_grid(self):
        """
        打印数独
        """
        for row in self.grid:
            print(' '.join(map(str, row)))

    def count_empty_cells(self):
        """
        统计数独中还有多少未填格
        """
        count = 0
        for row in range(self.GRID_SIZE):
            for col in range(self.GRID_SIZE):
                if self.grid[row][col] == 0:
                    count += 1
        return count
def test_sudoku():
    """
    测试代码和测试用例。
    """
    input_str = '017903600000080000900000507072010430000402070064370250701000065000030000005601720'
    sudoku = Sudoku(input_str)
    assert len(sudoku.grid) == Sudoku.GRID_SIZE
    assert len(sudoku.grid[0]) == Sudoku.GRID_SIZE
    cloned_sudoku = sudoku.clone()
    assert sudoku == cloned_sudoku
    serialized_str = sudoku.serialize()
    assert serialized_str == input_str
    candidates = sudoku.get_candidates(0, 0)
    assert isinstance(candidates, set)
    row = sudoku.getRow(0)
    assert len(row) == Sudoku.GRID_SIZE
    column = sudoku.getColumn(0)
    assert len(column) == Sudoku.GRID_SIZE
    box = sudoku.getBox(0, 0)
    assert len(box) == Sudoku.BOX_SIZE * Sudoku.BOX_SIZE
    print("All tests passed!")

if __name__ == '__main__':
    test_sudoku()