package leviathan.api.render;

import leviathan.utils.RendererRepeat;
import leviathan.utils.RendererSimple;
import leviathan.utils.RendererSliced;

public enum DrawMode {
	SIMPLE {
		@Override
		public ISpriteRenderer createRenderer(ISprite sprite) {
			return new RendererSimple(sprite);
		}
	},
	SLICED {
		@Override
		public ISpriteRenderer createRenderer(ISprite sprite) {
			return new RendererSliced(sprite, 16, 16, 16, 16);
		}
	},
	REPEAT {
		@Override
		public ISpriteRenderer createRenderer(ISprite sprite) {
			return new RendererRepeat(sprite);
		}
	};

	public abstract ISpriteRenderer createRenderer(ISprite sprite);
}
