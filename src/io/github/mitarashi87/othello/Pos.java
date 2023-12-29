package io.github.mitarashi87.othello;

import java.io.Serializable;

/**
 * 2次元座標管理
 */

public record Pos(
		int x,
		int y)
		implements Serializable {
}
