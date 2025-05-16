package ru.senla.javacourse.enchilik.scooterrental.core.tariff;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.TariffType;

@Component
public class TariffStrategyResolver {
    private static Map<TariffType, Class<? extends TariffStrategy>> classes = new EnumMap<>(TariffType.class);

    static {
        classes.put(TariffType.BASIC, BasicTariffStrategy.class);
        //        classes.put(TariffType.HOURLY, HourlyTariffStrategy.class);
        //        classes.put(TariffType.PREPAID_MINUTES, PrepaudMinutesTariffStrategy.class);
    }

    private final Map<TariffType, TariffStrategy> strategyBeans = new EnumMap<>(TariffType.class);

    private final ApplicationContext ctx;

    @Autowired
    public TariffStrategyResolver(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public TariffStrategy resolve(TariffType type) {
        return strategyBeans.computeIfAbsent(type, key -> ctx.getBean(classes.get(key)));
    }
}
