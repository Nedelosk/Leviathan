package leviathan.gui.widget;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import leviathan.api.gui.IWidget;
import leviathan.api.gui.IWindowWidget;
import leviathan.gui.events.EventValueChanged;
import leviathan.widgets.layouts.VerticalLayout;

/**
 * A element list with selectable elements.
 */
public class WidgetList<V> extends WidgetContainer {
	private final Map<V, IWidget> allOptions = new LinkedHashMap<>();
	private final Map<V, IWidget> visibleOptions = new LinkedHashMap<>();
	private final BiFunction<V, WidgetList, IWidget> optionFactory;
	@Nullable
	private final V defaultValue;
	@Nullable
	private V value;
	@Nullable
	private Predicate<V> validator;

	public WidgetList(int xPos, int yPos, int width, BiFunction<V, WidgetList, IWidget> optionFactory, @Nullable V defaultValue) {
		super(xPos, yPos, width, 0);
		setLayout(new VerticalLayout(0));
		this.optionFactory = optionFactory;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Nullable
	public V getCurrentValue() {
		return this.value;
	}

	public void setCurrentValue(@Nullable final V value) {
		if (value == this.value) {
			return;
		}
		this.value = value;
		/*if (this.optionWidgets.containsKey(value)) {
			final IWidget child = this.optionWidgets.get(value);
			this.parent.ensureVisible(child.getYPos(), child.getYPos() + child.getHeight(), this.getHeight());
		}*/
		IWindowWidget window = getWindow();
		window.dispatchEvent(new EventValueChanged<Object>(this, value));
	}

	public void updateVisibleOptions() {
		elements.clear();
		setHeight(0);
		visibleOptions.clear();

		for (Map.Entry<V, IWidget> entry : this.allOptions.entrySet()) {
			if (isVisible(entry.getKey())) {
				add(entry.getValue());
				visibleOptions.put(entry.getKey(), entry.getValue());
			} else {
				entry.getValue().setYPosition(0);
			}
		}
		setCurrentValue(this.getCurrentValue());
	}

	public boolean isVisible(V value) {
		return validator == null || validator.test(value);
	}

	public void setValidator(Predicate<V> validator) {
		if (this.validator != validator) {
			this.validator = validator;
			updateVisibleOptions();
		}
	}

	public int getIndexOf(@Nullable V value) {
		if (value == null) {
			return -1;
		}
		int index = 0;
		for (V option : this.getOptions()) {
			if (option.equals(value)) {
				return index;
			}
			++index;
		}
		return -1;
	}

	public int getCurrentIndex() {
		return this.getIndexOf(getCurrentValue());
	}

	public void setIndex(int currentIndex) {
		int index = 0;
		for (V option : this.getOptions()) {
			if (index == currentIndex) {
				this.setCurrentValue(option);
				return;
			}
			++index;
		}
		this.setCurrentValue(defaultValue);
	}

	public Collection<V> getOptions() {
		return visibleOptions.keySet();
	}

	public void setOptions(Collection<V> options) {
		clear();
		allOptions.clear();
		for (V option : options) {
			IWidget element = optionFactory.apply(option, this);
			allOptions.put(option, element);
		}
		updateVisibleOptions();
	}
}
