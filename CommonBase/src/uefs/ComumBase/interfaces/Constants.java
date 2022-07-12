package uefs.ComumBase.interfaces;

public interface Constants {

    /**
     * Refere-se aos status http.
     */
    public static final String STATUS = "STATUS";
    /**
     * Refere-se ao método REST.
     */
    public static final String METHOD = "METHOD";

    /**
     * Interface responsável por possibilitar o acesso aos métodos REST.
     *
     * @author Everton Bruno Silva dos Santos.
     * @version 1.0
     */
    public interface RestMethod {

        /**
         * Responsável por buscar dados.
         */
        public final static String GET = "GET";
        /**
         * Responsável por atualizar dados.
         */
        public final static String PUT = "PUT";
        /**
         * Responsável por criar dados.
         */
        public final static String POST = "POST";
        /**
         * Responsável por apagar dados.
         */
        public final static String DELETE = "DELETE";

    }

    /**
     * Interface responsável por possibilitar o acesso a StatusCodeHTTP.
     *
     * @author Everton Bruno Silva dos Santos.
     * @version 1.0
     */
    public interface HttpStatus {

        /**
         * O servidor pode processar a requisição com êxito.
         */
        public final static String OK = "200";
        /**
         * O servidor pode encontrar o recurso solicitado.
         */
        public final static String FOUND = "302";
        /**
         * O servidor não pode encontrar o recurso solicitado.
         */
        public final static String NOT_FOUND = "404";
        /**
         * O servidor não pode ou não irá processar a requisição devido a alguma
         * coisa que foi entendida como um erro do cliente.
         */
        public final static String BAD_REQUEST = "400";
        /**
         * O servidor não pode processar a requisição em tempo hábil.
         */
        public final static String REQUEST_TIMEOUT = "408";
        /**
         * O servidor encontrou uma situação com a qual não sabe lidar.
         */
        public final static String INTERNAL_SERVER_ERROR = "500";

    }

}
