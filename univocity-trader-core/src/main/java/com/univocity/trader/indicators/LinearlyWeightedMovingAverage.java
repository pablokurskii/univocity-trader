package com.univocity.trader.indicators;

import com.univocity.trader.candles.*;
import com.univocity.trader.indicators.base.*;
import com.univocity.trader.strategy.*;

import java.util.function.*;

public class LinearlyWeightedMovingAverage extends MultiValueIndicator {

	private double value;
	private final int length;

	public LinearlyWeightedMovingAverage(TimeInterval interval) {
		this(5, interval);
	}

	public LinearlyWeightedMovingAverage(int length, TimeInterval interval) {
		this(length, interval, c -> c.close);
	}

	public LinearlyWeightedMovingAverage(int length, TimeInterval interval, ToDoubleFunction<Candle> valueGetter) {
		super(length, interval, valueGetter);
		this.length = length;
	}

	@Override
	protected boolean calculateIndicatorValue(Candle candle, double value, boolean updating) {
		double sum = 0;
		double denominator = 0;

		double len = Math.min(getAccumulationCount() + 1, length);

		for (int i = 1; i <= len; i++) {
			double multiplier = (len - (i - 1));
			denominator += multiplier;
			sum += values.getRecentValue(i) * multiplier;
		}
		this.value = sum / denominator;

		return true;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	protected Indicator[] children() {
		return new Indicator[]{};
	}

}