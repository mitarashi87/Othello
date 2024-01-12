package io.github.mitarashi87.othello;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Board {
	private List<Square> squares;
	private Pos size;

	/**
	 * 盤面の生成
	 */
	public Board(Pos size) {
		this.size = size;
		int maxX = size.x();
		int maxY = size.y();
		squares = new ArrayList<>();
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				Square square = new Square(x, y);
				squares.add(square);
			}
		}
	}

	public Pos getSize() {
		return this.size;
	}

	/**
	 * 配置可能な座標を探す
	 * 
	 * @param disc 対象の色
	 */
	public List<Pos> canDropPositions(Disc disc) {
		List<Pos> canDropPositions = new ArrayList<>();
		for (Square square : squares) {
			Pos pos = square.getPos();
			if (canDrop(pos, disc)) {
				canDropPositions.add(pos);
			}
		}
		return canDropPositions;
	}

	/**
	 * 配置可能ならtrueを返す
	 * 
	 * @param pos 対象座標
	 * @param disc 色
	 */
	public boolean canDrop(Pos pos, Disc disc) {
		// 座標がボード外なら配置不可
		if (pos.x() < 0) return false;
		if (pos.y() < 0) return false;
		if (pos.x() >= size.x()) return false;
		if (pos.y() >= size.y()) return false;


		// squareにdiscがあれば配置不可
		Optional<Square> targetSquareOpt = this.getMayBeSquare(pos);
		if (targetSquareOpt.isEmpty()) return false;
		Square targetSquare = targetSquareOpt.get();
		if (targetSquare.hasDisc()) return false;

		// 置いた際にひっくり返せなければ配置不可
		List<Square> flipTargets = findFlipTargets(pos, disc);
		if (flipTargets.isEmpty()) return false;

		return true;
	}



	/**
	 * squareのdiscを指定の色に
	 */
	public void setDisc(Pos pos, Disc disc) {
		Optional<Square> squareOpt = this.getMayBeSquare(pos);
		if (squareOpt.isPresent()) {
			Square square = squareOpt.get();
			square.setDisc(disc);
		}
	}

	/**
	 * 盤面に石を置き周囲をひっくり返す
	 */
	public void dropDisc(Pos startPos, Disc disc) {
		this.setDisc(startPos, disc);

		List<Square> flipTargets = findFlipTargets(startPos, disc);
		for (Square square : flipTargets) {
			square.setDisc(disc);
		}
	}

	private Optional<Square> getMayBeSquare(Pos pos) {
		for (Square square : squares) {
			if (square.x != pos.x()) continue;
			if (square.y != pos.y()) continue;
			return Optional.of(square);
		}
		return Optional.empty();
	}

	/**
	 * ひっくり返すべきすべてのマスのリストを取得する
	 * 
	 * @param startPos 始点
	 * @param disc プレイヤーの色
	 * @return
	 */
	private List<Square> findFlipTargets(Pos startPos, Disc disc) {
		List<Pos> directions = List.of(
				new Pos(1, 0),
				new Pos(-1, 0),
				new Pos(0, 1),
				new Pos(0, -1),
				new Pos(1, 1),
				new Pos(-1, -1),
				new Pos(1, -1),
				new Pos(-1, 1));

		List<Square> flipTargets = new ArrayList<>();
		for (Pos direction : directions) {
			List<Square> flipTargetsParDirection =
					findFlipTargetsFromDirection(startPos, disc, direction);
			flipTargets.addAll(flipTargetsParDirection);
		}
		return flipTargets;

	}

	/**
	 * 指定した方向にあるひっくり返すべきマスのリストを取得する
	 * 
	 * @param startPos 始点
	 * @param disc プレイヤーの色
	 * @param direction 方向
	 */
	private List<Square> findFlipTargetsFromDirection(Pos startPos, Disc disc, Pos direction) {
		// 無限ループになることへの対策
		if (direction.x() == 0 && direction.y() == 0) return List.of();

		List<Square> passedSquares = new ArrayList<>();
		for (int scalar = 1; true; scalar++) {
			Pos moved = new Pos(
					direction.x() * scalar,
					direction.y() * scalar);
			Pos pos = new Pos(
					startPos.x() + moved.x(),
					startPos.y() + moved.y());
			Optional<Square> mayBeSquare = getMayBeSquare(pos);
			// Square (盤上の座標) が無ければ終了
			if (!mayBeSquare.isPresent()) return List.of();
			Square square = mayBeSquare.get();
			// Square に Disc が置かれていなければ終了
			if (!square.hasDisc()) return List.of();
			// Square に同じ Disc が置かれていたらここまで通ったSquareを返す
			if (square.hasSameDisc(disc)) return passedSquares;
			// Squareに違うDiscが置かれていたら経路を控えてもう一マス進む
			passedSquares.add(square);
		}
	}

	/**
	 * 石ごとに個数をカウントして返す
	 */
	public Map<Disc, Integer> getCountParDisc(Set<Disc> countTargets) {
		Map<Disc, Integer> countParDisc = new LinkedHashMap<>();
		for (Disc disc : countTargets) {
			countParDisc.put(disc, 0);
		}

		for (Square square : squares) {
			for (Disc disc : countTargets) {
				if (square.hasSameDisc(disc)) {
					// 石数を +1
					Integer currentCount = countParDisc.get(disc);
					Integer nextCount = currentCount + 1;
					countParDisc.put(disc, nextCount);
				}
			}
		}

		return countParDisc;
	}

	/**
	 * 表示
	 */
	@Override
	public String toString() {
		int maxX = this.size.x();
		Integer maxY = this.size.y();
		String str = maxY.toString();

		String boardString = getStringColumnHeader(maxX, str.length()) + "\n";
		for (int y = 0; y < maxY; y++) {
			boardString += getStringRowHeader(y, str.length());
			for (int x = 0; x < maxX; x++) {
				for (Square square : squares) {
					if (square.x == x && square.y == y) {
						boardString += square.toString();
					}
				}
			}
			boardString += "\n";
		}
		return boardString;
	}

	/**
	 * 盤のx軸のヘッダー文字列の全体を取得
	 * 
	 * @param maxX - x座標の最大値
	 * @param rowHeaderLength - y座標ヘッダーの最大幅
	 */
	private String getStringColumnHeader(
			int maxX,
			int rowHeaderLength) {
		String firstSpacing = " ".repeat(rowHeaderLength);

		String columnHeader = firstSpacing;
		for (int index = 0; index < maxX; index++) {
			String indexStr = Integer.toString(index);
			if (indexStr.length() < 2) {
				columnHeader += " " + indexStr;
			} else {
				columnHeader += indexStr;
			} ;
		}

		return columnHeader;
	}

	/**
	 * 盤のy軸のヘッダー文字列を1列分取得
	 * 
	 * @param y - y座標
	 * @param length - y座標ヘッダーの最大幅
	 */
	private String getStringRowHeader(int y, int length) {
		String yStr = Integer.toString(y);
		int lengthDiff = length - yStr.length();
		String spacing = " ".repeat(lengthDiff);
		return spacing + yStr;
	}

}
