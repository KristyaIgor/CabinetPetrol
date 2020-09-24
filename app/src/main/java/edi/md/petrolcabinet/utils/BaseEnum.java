package edi.md.petrolcabinet.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tony on 2017/12/3.
 */

public class BaseEnum {
    public static final int  PersoanaFizica = 0 ,PersoanaJuridica = 1, PerCard = 2;
    public static final int  LimitBani = 0 , limitCantitate = 1;
    public static final int  chipAll = 55 , chipAlimentare = 65, chipSuplinire = 75, chipDay = 15, chipWeek = 25, chipMonth = 35, chipYear = 45 , chipAllPeriod = 85;

    @IntDef({PersoanaFizica, PersoanaJuridica, PerCard})
    @Retention(RetentionPolicy.SOURCE)
    public @interface typePersona {
    }

    @IntDef({LimitBani, limitCantitate})
    @Retention(RetentionPolicy.SOURCE)
    public @interface typeLimit {
    }

    @IntDef({chipAll, chipAlimentare, chipSuplinire, chipDay, chipWeek, chipMonth, chipYear, chipAllPeriod})
    @Retention(RetentionPolicy.SOURCE)
    public @interface chipFilter {
    }
}
