class ServerCommon {
	private static int[][] cell = new int[4][4];

	public static String Analyze(String key, String string) {
		ServerCommon.StringToCell(string);
		if (key.equals("UP")) ServerCommon.CellToUp();
		if (key.equals("DOWN")) ServerCommon.CellToDown();
		if (key.equals("LEFT")) ServerCommon.CellToLeft();
		if (key.equals("RIGHT")) ServerCommon.CellToRight();
		if (key.equals("NONE")) {}
		
		if (ServerCommon.CheckCellFull()) {
			System.out.println("LOSE!!!");
			Server.setState(false);
		}
	
		return ServerCommon.CellToString();
	}

	public static String GenerateString() {
		int x_1 = ServerCommon.RandomInteger() % 4;
		int y_1 = ServerCommon.RandomInteger() % 4;
		int x_2 = ServerCommon.RandomInteger() % 4;
		int y_2 = ServerCommon.RandomInteger() % 4;
		if (x_2 == x_1) {
			while (y_2 == y_1) {
				y_2 = ServerCommon.RandomInteger() % 4;
			}
		}
		String string = "";
		for (int i = 0; i < 16; i++) {
			if (x_1 * 4 + y_1 == i) {
				int value = ServerCommon.RandomInteger() % 10;
				if (value % 5 == 0) string += "4;";
				else string += "2;";
			}
			else {
				if (x_2 * 4 + y_2 == i) {
					int value = ServerCommon.RandomInteger() % 10;
					if (value % 5 == 0) string += "4;";
					else string += "2;";
				}
				else string += "0;";
			}
		}
		return string;
	}

	private static int RandomInteger() {
		return (int)(Math.random() * 1000);
	}

	private static void StringToCell(String string) {
		int column = 0, row = 0, point = 0;
		for (int index = 0; index < string.length(); index++) {
			if (string.charAt(index) == ';') {
				cell[row][column] = Integer.parseInt(string.substring(point, index));
				point = index + 1;
				column++;
				if (column == 4) {
					column = 0;
					row++;
				}
				if (row == 4) break;
			}
		}
	}

	private static String CellToString() {
		String string = "";
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				string += (String.valueOf(cell[r][c]) + ";");
			}	
		}
		return string;
	}

	private static void CellToUp() {
		boolean isChanged = false;
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j >= 0; j--) {
				if (cell[j][i] == 0) {
					for (int k = j + 1; k < 4; k++) {
						if (cell[k][i] != 0) isChanged = true;
						cell[k - 1][i] = cell[k][i];
					}
					cell[3][i] = 0;
				}
			}
			if (cell[0][i] == cell[1][i] && cell[0][i] != 0) {
				cell[0][i] *= 2;
				isChanged = true;
				Server.setScore(cell[0][i] + Server.getScore());
				if (cell[2][i] == cell[3][i] && cell[2][i] != 0) {
					cell[1][i] = cell[2][i] * 2;
					Server.setScore(cell[1][i] + Server.getScore());
					cell[2][i] = cell[3][i] = 0;
				}
				else {
					cell[1][i] = cell[2][i];
					cell[2][i] = cell[3][i];
					cell[3][i] = 0;
				}
			}
			if (cell[1][i] == cell[2][i] && cell[1][i] != 0) {
				cell[1][i] *= 2;
				isChanged = true;
				Server.setScore(cell[1][i] + Server.getScore());
				cell[2][i] = cell[3][i];
				cell[3][i] = 0;
			}
			if (cell[2][i] == cell[3][i] && cell[2][i] != 0) {
				cell[2][i] *= 2;
				isChanged = true;
				Server.setScore(cell[2][i] + Server.getScore());
				cell[3][i] = 0;
			}
		}
		if (isChanged == true) ServerCommon.GenerateCell();
	}
	
	private static void CellToDown() {
		boolean isChanged = false;
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j <=3; j++) {
				if (cell[j][i] == 0) {
					for (int k = j - 1; k >= 0; k--) {
						if (cell[k][i] != 0) isChanged = true;
						cell[k + 1][i] = cell[k][i];
					}
					cell[0][i] = 0;
				}
			}
			if (cell[3][i] == cell[2][i] && cell[3][i] != 0) {
				isChanged = true;
				cell[3][i] *= 2;
				Server.setScore(cell[3][i] + Server.getScore());
				if (cell[1][i] == cell[0][i] && cell[1][i] != 0) {
					cell[2][i] = cell[1][i] * 2;
					Server.setScore(cell[2][i] + Server.getScore());
					cell[1][i] = cell[0][i] = 0;
				}
				else {
					cell[2][i] = cell[1][i];
					cell[1][i] = cell[0][i];
					cell[0][i] = 0;
				}
			}
			if (cell[2][i] == cell[1][i] && cell[2][i] != 0) {
				cell[2][i] *= 2;
				isChanged = true;
				Server.setScore(cell[2][i] + Server.getScore());
				cell[1][i] = cell[0][i];
				cell[0][i] = 0;
			}
			if (cell[1][i] == cell[0][i] && cell[1][i] != 0) {
				cell[1][i] *= 2;
				isChanged = true;
				Server.setScore(cell[1][i] + Server.getScore());
				cell[0][i] = 0;
			}
		}
		if (isChanged == true) ServerCommon.GenerateCell();
	}

	private static void CellToLeft() {
		boolean isChanged = false;
		for (int i = 0; i < 4; i++) {
			for (int j = 2; j >= 0; j--) {
				if (cell[i][j] == 0) {
					for (int k = j + 1; k < 4; k++) {
						if (cell[k][i] != 0) isChanged = true;
						cell[i][k - 1] = cell[i][k];
					}
					cell[i][3] = 0;
				}
			}
			if (cell[i][0] == cell[i][1] && cell[i][0] != 0) {
				cell[i][0] *= 2;
				isChanged = true;
				Server.setScore(cell[i][0] + Server.getScore());
				if (cell[i][2] == cell[i][3] && cell[i][2] != 0) {
					cell[i][1] = cell[i][2] * 2;
					Server.setScore(cell[i][1] + Server.getScore());
					cell[i][2] = cell[i][3] = 0;
				}
				else {
					cell[i][1] = cell[i][2];
					cell[i][2] = cell[i][3];
					cell[i][3] = 0;
				}
			}
			if (cell[i][1] == cell[i][2] && cell[i][1] != 0) {
				cell[i][1] *= 2;
				isChanged = true;
				Server.setScore(cell[i][1] + Server.getScore());
				cell[i][2] = cell[i][3];
				cell[i][3] = 0;
			}
			if (cell[i][2] == cell[i][3] && cell[i][2] != 0) {
				cell[i][2] *= 2;
				isChanged = true;
				Server.setScore(cell[i][2] + Server.getScore());
				cell[i][3] = 0;
			}
		}
		if (isChanged == true) ServerCommon.GenerateCell();
	}

	private static void CellToRight() {
		boolean isChanged = false;
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j <= 3; j++) {
				if (cell[i][j] == 0) {
					for (int k = j - 1; k >= 0; k--) {
						if (cell[k][i] != 0) isChanged = true;
						cell[i][k + 1] = cell[i][k];
					}
					cell[i][0] = 0;
				}
			}
			if (cell[i][3] == cell[i][2] && cell[i][3] != 0) {
				cell[i][3] *= 2;
				isChanged = true;
				Server.setScore(cell[i][3] + Server.getScore());
				if (cell[i][1] == cell[i][0] && cell[i][1] != 0) {
					cell[i][2] = cell[i][1] * 2;
					Server.setScore(cell[2][0] + Server.getScore());
					cell[i][1] = cell[i][0] = 0;
				}
				else {
					cell[i][2] = cell[i][1];
					cell[i][1] = cell[i][0];
					cell[i][0] = 0;
				}
			}
			if (cell[i][2] == cell[i][1] && cell[i][2] != 0) {
				cell[i][2] *= 2;
				isChanged = true;
				Server.setScore(cell[i][2] + Server.getScore());
				cell[i][1] = cell[i][0];
				cell[i][0] = 0;
			}
			if (cell[i][1] == cell[i][0] && cell[i][1] != 0) {
				cell[i][1] *= 2;
				isChanged = true;
				Server.setScore(cell[i][1] + Server.getScore());
				cell[i][0] = 0;
			}
		}
		if (isChanged == true) ServerCommon.GenerateCell();
	}

	public static boolean CheckIsWin() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (cell[i][j] == 2048) return true;
			}
		}
		return false;
	}
	
	private static boolean CheckCellFull() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (cell[i][j] == 0) return false;
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (cell[i][j] == cell[i + 1][j]) return false;
				if (cell[i][j] == cell[i][j + 1]) return false;
			}
		}
		for (int i = 0; i < 3; i++) {
			if (cell[3][i] == cell[3][i + 1]) return false;
			if (cell[i][3] == cell[i + 1][3]) return false;
		}
		return true;
	}
	
	private static void GenerateCell() {
		int r = ServerCommon.RandomInteger() % 4;
		int c = ServerCommon.RandomInteger() % 4;
		while(cell[r][c] != 0) {
			r = ServerCommon.RandomInteger() % 4;
			c = ServerCommon.RandomInteger() % 4;
		}	
		int tmp = ServerCommon.RandomInteger() % 10;
		if (tmp % 5 == 0) cell[r][c] = 4;
		else cell[r][c] = 2;
	}
}
