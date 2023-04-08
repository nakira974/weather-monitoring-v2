package coffee.lkh.weathermonitoringv2.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WeatherforecastsNotFoundException extends RuntimeException {
    public WeatherforecastsNotFoundException() {
        super();
    }
    public WeatherforecastsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public WeatherforecastsNotFoundException(String message) {
        super(message);
    }
    public WeatherforecastsNotFoundException(Throwable cause) {
        super(cause);
    }
}
