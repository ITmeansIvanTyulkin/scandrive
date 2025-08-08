package sqlqueries;

/*
SQL-запросы в базу данных.
 */
public class SQLCode {
    // Запрос в таблицу 'orders'.
    public static final String GET_FROM_ORDERS = """
                        
            FROM sub_orders
            """;

    // Запрос в таблицу 'device'.
    public static final String GET_FROM_DEVICE = """
                        
            FROM device
            """;

    public static final String GET_ANY_FIELD_NAME = """

            FROM device
            """;

// Агрегация.
    // Проверка на существования 'id' в базе данных в таблице 'aggregation_line'.
    public static final String IF_ID_EXISTS = """
            SELECT *
            FROM aggregation_line
            WHERE id =\s
            """;
    // Удаление тестовых линий агрегации из таблицы 'aggregation_line_buffer_rule' базы данных.
    public static final String CLEAN_AGGREGATION_LINE_BUFFER_RULE = """
                        
            FROM aggregation_line_buffer_rule
            """;
    // Удаление тестовых линий агрегации из таблицы 'aggregation_line_buffered_codes' базы данных.
    public static final String CLEAN_AGGREGATION_LINE_BUFFERED_CODES = """
                        
            FROM aggregation_line_buffered_codes
            """;
    // Удаление тестовых линий агрегации из таблицы 'buffered_codes' базы данных.
    public static final String CLEAN_BUFFERED_CODES = """
                        
            FROM buffered_codes
            """;
    // Удаление тестовых линий агрегации из таблицы 'gs1_128_rules_to_aggregation_lines' базы данных.
    public static final String CLEAN_GS1_128_RULES_TO_AGGREGATION_LINES = """
                        
            FROM gs1_128_rules_to_aggregation_lines
            """;
    // Удаление тестовых линий агрегации из таблицы 'monitors_to_lines' базы данных.
    public static final String CLEAN_MONITORS_TO_LINES = """
                        
            FROM monitors_to_lines
            """;
    // Удаление тестовых линий агрегации из таблицы 'filtration_rule' базы данных.
    public static final String CLEAN_FILTRATION_RULE = """
                        
            FROM filtration_rule
            """;
    // Удаление тестовых линий агрегации из таблицы 'aggregation_line' базы данных.
    public static final String CLEAN_AGGREGATION_LINE = """
                        
            FROM aggregation_line
            """;

// Hook controller.
    // Запрос в базу данных, что распарсенный 'id' присутствует (существует).
    public static final String IF_ID_EXISTS_IN_DEVICE_TABLE = """
            SELECT id
            FROM device
            WHERE name = \s
            """;

// Aggregation GTIN controller.
    // Запрос в базу данных с конкретным 'GTIN', чтобы убедиться, что он существует в БД.
    public static final String IF_GTIN_EXISTS = """
            SELECT gtin
            FROM aggregation_gtin
            where gtin =\s
            """;
    // Проверка, база данных пустая.
    public static final String IS_EMPTY = """
             gtin
             FROM aggregation_gtin
            """;

// Best before date controller.
    // Запрос в базу данных, что в ней появились тестовые 'GTIN'.
    public static final String IS_EXISTED = """
            gtin
            FROM best_before_date
            """;

// Monitor controller.
    // Запрос в базу данных, что в ней появляется тестовый монитор.
    public static final String EXISTS = """
            SELECT *
            FROM monitors
            WHERE id = \s
            """;
    // Удаление тестовых данных из базы данных из таблицы 'monitor_algorithm_properties'.
    public static final String CLEAN_DATA_BASE_MONITOR_ALGORITHM_PROPERTIES = """
                        
            FROM monitor_algorithm_properties
            """;
    // Удаление тестовых данных из базы данных из таблицы 'monitors'.
    public static final String CLEAN_DATA_BASE_MONITOR = """
                        
            FROM monitors
            """;

// Orders controller.
    // Запрос в базу данных, что в ней появляется тестовый заказ.
    public static final String ORDER_EXISTS = """
            SELECT *
            FROM orders
            WHERE id = \s
            """;
    // Удаление тестовых данных из базы данных из таблицы 'orders'.
    public static final String CLEAN_DATA_BASE_ORDERS = """
                        
            FROM orders
            """;

// Printer controller.
    // Запрос в базу данных, что в ней появляется тестовый принтер.
    public static final String PRINTER_EXISTS = """
            SELECT *
            FROM printer
            WHERE id = \s
            """;
    // Запрос в базу данных для получения данных из таблицы 'printer' для проверки и сравнения с 'JSON'.
    public static final String GET_DATA_FROM_PRINTER_TABLE = """
            SELECT id, ip, "template", "name", port, zpl_sscc_prefix, zpl_gs1_128_prefix, zpl_gs1_databar_expanded_prefix, zpl_gs_separator_replacer
            FROM printer
            """;
    // Удаление тестовых данных из базы данных из таблицы 'printer'.
    public static final String CLEAN_DATA_BASE_PRINTER = """
                        
            FROM printer
            """;

// Testing controller.
    // Удаление рабочей станции
    public static final String CLEAN_DATA_BASE_WORK_STATION = """
                        
            FROM workstation
            """;
    // Извлечение 10 кодов из таблицы 'datamatrix' из 'conveyor-core'.
    public static final String GET_10_CODES_FROM_DATAMATRIX = """
                        
            FROM datamatrix
            LIMIT 10
            """;
    // Удаление сканера.
    public static final String CLEAN_DATA_BASE_SCANNER = """
                        
            FROM scanner
            """;
    // Удаление шаблона печати (из таблицы 'print_template').
    public static final String CLEAN_DATA_BASE_PRINT_TEMPLATE = """
                        
            FROM print_template
            """;

// Print template controller.
    // Запрос в базу данных, что в ней появляется шаблон печати.
    public static final String PRINT_TEMPLATE_EXISTS = """
            SELECT *
            FROM print_template
            WHERE id = \s
            """;
// Scanner controller.
    // Запрос в базу данных на проверку существования сканера.
    public static final String SCANNER_EXISTS = """
        SELECT *
        FROM scanner
        WHERE id = \s
        """;

// System properties controller V2.
    // Запрос в базу данных на проверку существования системного реквизита.
    public static final String SYS_PROP_EXISTS = """
        SELECT *
        FROM sys_prop
        WHERE id = \s
        """;
    // Проверка, что обновлённое значение действительно присутствует в базе данных в колонке 'prop_value'.
    public static final String UPDATED_EXISTS = """
            SELECT COUNT(*) AS count
            FROM sys_prop
            WHERE prop_value LIKE '%%UPDATED%%'
            """;
    // Удаление тестовых данных системного реквизита (из таблицы 'sys_prop').
    public static final String CLEAN_DATA_BASE_SYS_PROP = """
                        
            FROM sys_prop
            """;

// Workstation controller.
    // Проверка существования созданной рабочей станции в базе данных.
    public static final String WORKSTATION_EXISTS = """
        SELECT *
        FROM workstation
        WHERE id = \s
        """;
    // Получаю 'id' шаблона печати из таблицы 'print_template'.
    public static final String GET_ID_FROM_PRINT_TEMPLATE = """
            
            FROM print_template
            """;
    // Получаю 'id' принтера из таблицы 'printer'.
    public static final String GET_ID_FROM_PRINTER = """
            
            FROM printer
            """;
    // Получаю 'id' принтера из таблицы 'scanner'.
    public static final String GET_ID_FROM_SCANNER = """
            
            FROM scanner
            """;
    // Проверка, что обновлённое значение действительно присутствует в базе данных в колонке 'name'.
    public static final String UPDATED_NAME_EXISTS = """
            SELECT COUNT(*) AS count
            FROM workstation
            WHERE name LIKE '%%UPDATED%%'
            """;

// Datamatrix controller.
    // Удаление тестовых данных из таблицы 'params' ('datamatrix').
    public static final String CLEAN_TEST_DATA_IN_PARAMS = """
        
        FROM params
        """;
    // Удаление тестовых данных из таблицы 'device' ('datamatrix').
    public static final String CLEAN_TEST_DATA_IN_DEVICE = """
        
        FROM device
        """;
    // Удаление тестовых данных из таблицы 'hooks' ('datamatrix').
    public static final String CLEAN_TEST_DATA_IN_HOOKS = """
        
        FROM hooks
        """;

// Device controller V2.
    // Проверка на существования принтера в базе данных в таблице 'device'.
    public static final String DEVICE_EXISTS = """
        SELECT *
        FROM device
        WHERE id = \s
        """;
    // Запрос в базу данных для получения данных из таблицы 'device' для проверки и сравнения с 'JSON'.
    public static final String GET_DATA_FROM_DEVICE_TABLE = """
            SELECT id, name, url, printer_type, batch_dm, production_date, send_printed_codes_to_queue, state
            FROM device
            """;
    // Запрос в базу данных, что в ней появляется тестовое устройство.
    public static final String TEST_DEVICE_EXISTS = """
            SELECT *
            FROM device
            WHERE id = \s
            """;
    // Получить 'id' устройства из базы данных.
    public static final String GET_ID_FROM_DATABASE = """
            
            FROM device
            """;

// Printer locks controller.
    // Получение значения поля 'name'  из таблицы 'device'.
    public static final String GET_DEVICE_NAME = """
        SELECT name
        FROM device
        WHERE id = \s
        """;
    // Удаление тестовых данных из таблицы 'printer_locks' ('datamatrix').
    public static final String CLEAN_TEST_DATA_IN_PRINTER_LOCKS = """
        
        FROM printer_locks
        """;

// Print job controller.
    // Проверка на существование созданного заказа в базе данных.
public static final String ORDER_EXISTS_IN_PRINT_JOB_CONTROLLER = """
            SELECT *
            FROM sub_orders
            WHERE id = \s
            """;
    // Проверка на существования задания на печать в базе данных.
    public static final String PRINT_TASK_EXISTS = """
            SELECT *
            FROM print_job
            WHERE id = \s
            """;
    // Удаление тестовых данных из базы данных из таблицы 'sub_orders'.
    public static final String CLEAN_DATA_BASE_SUB_ORDERS = """
                        
            FROM sub_orders
            """;
    // Удаление тестовых данных из базы данных из таблицы 'sub_orders'.
    public static final String CLEAN_DATA_BASE_PRINT_JOB = """
                        
            FROM print_job
            """;
    // Удаление тестовых данных из базы данных из таблицы 'datamatrix'.
    public static final String CLEAN_DATA_BASE_DATAMATRIX = """
                        
            FROM datamatrix
            """;

// Templates controller.
    // Проверка по 'id', что шаблон появился в базе данных.
    public static final String TEMPLATE_EXISTS_IN_TEMPLATE_CONTROLLER = """
        SELECT *
        FROM "template"
        WHERE id = \s
        """;
    // Получение значения поля 'name'  из таблицы 'template'.
    public static final String GET_DEVICE_NAME_FROM_TEMPLATE = """
        SELECT name
        FROM template
        WHERE id = \s
        """;
    // Проверка, что обновлённое значение действительно присутствует в базе данных в колонке 'template'.
    public static final String UPDATED_NAME_EXISTS_IN_TEMPLATE = """
            SELECT COUNT(*) AS count
            FROM template
            WHERE name LIKE '%%UPDATED%%'
            """;
    // Удаление тестовых данных из базы данных из таблицы 'template'.
    public static final String CLEAN_DATA_BASE_TEMPLATE = """
                        
            FROM template
            """;

// Order import to CMS.
    // Удаление тестовых данных из базы данных из таблицы 'datamatrix' из базы 'CMS'.
    public static final String DELETE_TEST_DATA_FROM_CMS_DATAMATRIX = """
        
        FROM datamatrix
        """;
    // Удаление тестовых данных из базы данных из таблицы 'production_tasks' из базы 'CMS'.
    public static final String DELETE_TEST_DATA_FROM_CMS_PRODUCTION_TASKS = """
            
            FROM production_tasks
            """;

// Loading codes via RabbitMQ.
    // Удаление тестовых данных (загруженных через RabbitMQ кодов) из таблицы 'external_datamatrix_codes'.
public static final String CLEAN_DATA_BASE_EXTERNAL_DATAMATRIX_CODES = """
                        
            FROM external_datamatrix_codes
            """;

// Sub order controller..
    // Получение значения поля 'order_number'  из таблицы 'orders'.
    public static final String GET_ORDER_NUMBER_FROM_ORDERS = """
         order_number
        FROM orders
        WHERE id = \s
        """;
    // Проверка, что обновлённое значение действительно присутствует в базе данных в колонке 'code_type'.
    public static final String UPDATED_IN_CODE_TYPE_EXISTS = """
            SELECT COUNT(*) AS count
            FROM sub_orders
            WHERE code_type LIKE '%%GS-128%%'
            """;
    // Удаление тестовых данных из таблицы 'codes_for_validation'.
    public static final String CLEAN_DATA_BASE_CODES_FOR_VALIDATION = """
                        
            FROM codes_for_validation
            """;
}