package main.network;

import src.main.java.Request;
import src.main.java.Response;
import main.processor.AuthenticationProcessor;
import main.processor.CarAppProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class URL {

    static final Map<String, Route> routes = new HashMap<>();
    static {
        routes.put("user/login",            new Route(true, false, AuthenticationProcessor.class));
        routes.put("user/register",         new Route(true, false, AuthenticationProcessor.class));
        routes.put("user/logout",           new Route(false, true, AuthenticationProcessor.class));
        routes.put("user/configureHmac",    new Route(true, false, AuthenticationProcessor.class));

        routes.put("car-admin/add",         new Route("ADMIN", false, true, CarAppProcessor.class));
        routes.put("car-admin/delete",      new Route("ADMIN", false, true, CarAppProcessor.class));
        routes.put("car-admin/update",      new Route("ADMIN", false, true, CarAppProcessor.class));

        routes.put("car/details",           new Route(true, false, CarAppProcessor.class));
        routes.put("car/list",              new Route(true, false, CarAppProcessor.class));
        routes.put("car/search",            new Route(true, false, CarAppProcessor.class));

        routes.put("car-customer/buy",      new Route("CUSTOMER", false, true, CarAppProcessor.class));
    }

    public static class Route {
        private final String permission;
        private final boolean _public;
        private final boolean _signed;
        private final Class<? extends Function<Request, Response>> processor;

        private Route(
                final String permission,
                boolean p,
                boolean s,
                Class<? extends Function<Request, Response>> processor
        ) {
            this.permission = permission;
            this._public = p;
            this._signed = s;
            this.processor = processor;
        }

        private Route(
                boolean p,
                boolean s,
                Class<? extends Function<Request, Response>> processor
        ) {
            this.permission = null;
            this._public = p;
            this._signed = s;
            this.processor = processor;
        }

        public boolean needPermission() {
            return this.permission != null;
        }

        public String permission() {
            return this.permission;
        }

        public boolean is_public() {
            return this._public;
        }

        public boolean is_signed() {
            return this._signed;
        }

        public Class<? extends Function<Request, Response>> processor() {
            return this.processor;
        }
    }
}
