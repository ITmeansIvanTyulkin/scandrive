package data.endpoints.apiendpoints;

public abstract class Swagger {

    private static String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }

    // Базовый URI.
    public static final String BASE_URI_TEST_STAGE_8080 = getEnvOrDefault("BASE_URI_TEST_STAGE_8080", "http://91.109.201.205:8080");
    public static final String BASE_URI_TEST_STAGE_8088 = getEnvOrDefault("BASE_URI_TEST_STAGE_8088", "http://91.109.201.205:8088");
    public static final String BASE_URI_TEST_STAGE_65344 = getEnvOrDefault("BASE_URI_TEST_STAGE_65344", "http://91.109.201.205:65344");
    public static final String BASE_URI_TEST_STAGE = getEnvOrDefault("BASE_URI_TEST_STAGE", "http://91.109.201.205");
    public static final String BASE_TEST_PRINTER_IP = getEnvOrDefault("BASE_TEST_PRINTER_IP", "127.0.0.1");
    public static final String BASE_TEST_PRINTER_PORT = getEnvOrDefault("BASE_TEST_PRINTER_PORT", "9100");

    // Еndpoint-controller.
    // Контекстный поиск.
    public static final String CONTEXT_SEARCH = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints?");
    // Типы.
    public static final String GET_ALL_DATA_TYPES = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints/types");
    // Параметры.
    public static final String GET_ALL_PARAMETERS = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints/params-keys?endpoint_type=");
    // Добавить.
    public static final String ADD_NEW_POINT = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints");
    // Удалить.
    public static final String DELETE_POINT = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints/");
    // Получить информацию по 'id'.
    public static final String GET_INFO_BY_ID = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints/");
    // Обновить информацию.
    public static final String UPDATE_POINT_INFO = BASE_URI_TEST_STAGE_8080.concat("/core-reader/endpoints/");
    // Создание маршрута.
    public static final String MAKE_A_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/routes");
    // Удаление маршрута.
    public static final String DELETE_A_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/routes/");
    // Добавить дополнительную информацию к существующему маршруту.
    public static final String ADD_NEW_INFO_TO_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/additional-info/add-or-update-additional-info");
    // Удалить дополнительную информацию из существующего маршрута.
    public static final String DELETE_ADDITIONAL_INFO_FROM_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/additional-info/delete-additional-info");

// Timezone controller.
    // Получить все часовые пояса.
    public static final String GET_TIMEZONE_ALL = BASE_URI_TEST_STAGE.concat(":8090/core-reader/timezone/all");
    // Получить текущий часовой пояс.
    public static final String GET_CURRENT_TIMEZONE = BASE_URI_TEST_STAGE.concat(":8090/core-reader/timezone/current");
    // Обновить часовой пояс.
    public static final String RENEW_TIMEZONE = BASE_URI_TEST_STAGE.concat(":8090/core-reader/timezone/update");
    // Использование системного часового пояса.
    public static final String USE_SYSTEM_TIMEZONE = BASE_URI_TEST_STAGE.concat(":8090/core-reader/timezone/use-system");

// Агрегация core-aggregator
    // Добавить новую тестовую линию агрегации.
    public static final String ADD_NEW_TEST_AGGREGATION_LINE = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/aggregation-lines");

// Root locks controller.
    // Блокировка существующего маршрута.
    public static final String ROUTE_BLOCKING = BASE_URI_TEST_STAGE_8080.concat("/core-reader/root-locks");
    // Разблокировка существующего маршрута.
    public static final String ROUTE_UNBLOCKING = BASE_URI_TEST_STAGE_8080.concat("/core-reader/root-locks/");
    // Запросить список установленных блокировок на маршруты.
    public static final String GET_BLOCKS_LIST = BASE_URI_TEST_STAGE_8080.concat("/core-reader/root-locks?");
    // Обновить тип и причину блокировки для маршрута.
    public static final String UPDATE_TYPE_AND_REASON_OF_BLOCKING_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/root-locks/");

// Root controller.
    // Получение всех типов маршрутов.
    public static final String GET_ALL_ROUTE_TYPES = BASE_URI_TEST_STAGE_8080.concat("/core-reader/route/all-active-route-types");
    // Получение всех активных маршрутов (технические данные).
    public static final String GET_ALL_ACTIVE_ROUTES = BASE_URI_TEST_STAGE_8080.concat("/core-reader/route/all-active-routes-runtime");
    // Получение типов входящих точек.
    public static final String GET_INCOMING_POINTS_TYPES = BASE_URI_TEST_STAGE_8080.concat("/core-reader/route/get-from-endpoints-allowed-types?route_type=");
    // Получение типов исходящих точек.
    public static final String GET_OUTCOMING_POINTS_TYPES = BASE_URI_TEST_STAGE_8080.concat("/core-reader/route/get-to-endpoints-allowed-types?route_type=");
    // Перезапуск существующего маршрута.
    public static final String RESTART_EXISTING_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/route/restart-route");
    // Сброс счётчика существующего маршрута.
    public static final String RESET_ROUTE_COUNT = BASE_URI_TEST_STAGE_8080.concat("/core-reader/route/reset-counters");

// Root controller V2.
    // Получение маршрута.
    public static final String GET_ROUTE = BASE_URI_TEST_STAGE_8080.concat("/core-reader/routes?");
    // Получение информации о маршруте по его 'id'.
    public static final String GET_ROUTE_INFO_BY_ID = BASE_URI_TEST_STAGE_8080.concat("/core-reader/routes/");
    // Обновление информации о маршруте.
    public static final String UPDATE_ROUTE_INFO = BASE_URI_TEST_STAGE_8080.concat("/core-reader/routes/");

// Hook controller.
    // Создание и обновление хука.
    public static final String CREATE_AND_UPDATE_HOOK = BASE_URI_TEST_STAGE_8088.concat("/core-printer/create-or-update-hook");
    // Получаю все существующие хуки.
    public static final String GET_ALL_EXISTING_HOOKS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/hooks");
    // Удаляю хук по его 'id'.
    public static final String DELETE_HOOK = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-hook");

// Aggregation GTIN controller.
    // Добавляю аггрегационный 'GTIN'.
    public static final String ADD_AGGREGATION_GTIN = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/aggregation-gtin/add");
    // Получаю список всех аггрегационных 'GTIN'.
    public static final String GET_AGGREGATION_GTIN_LIST = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/aggregation-gtin/all");
    // Удаляю список всех аггрегационных 'GTIN'.
    public static final String DELETE_ALL_GTINS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/aggregation-gtin/delete");

// Best before date controller.
    // Добавление или изменения даты срока годности.
    public static final String ADD_OR_UPDATE_EXPIRATION_GTIN = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/best-before-date/add-or-update");
    // Получение всех данных о датах срока годности.
    public static final String GET_ALL_EXPIRATION_GTIN_DATA = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/best-before-date/all");
    // Получение списка дат срока годности 'GTIN'.
    public static final String GET_EXPIRATION_GTIN_LIST = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/best-before-date/by-gtin?");
    // Удаление 'GTIN'.
    public static final String DELETE_GTIN = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/best-before-date/delete-bbd");

// Monitor controller.
    // Контекстный поиск по мониторам.
    public static final String SEARCH_FOR_MONITORS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors?");
    // Добавление тестового монитора.
    public static final String ADD_TEST_MONITOR = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors");
    // Получить информацию о мониторе.
    public static final String GET_MONITOR_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/");
    // Обновить информацию о мониторе.
    public static final String UPDATE_MONITOR_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/");
    // Удалить монитор.
    public static final String DELETE_MONITOR = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/");
    // Получение всех уровней информации.
    public static final String GET_ALL_LEVELS_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/inform-levels");
    // Получение всех протоколов монитора.
    public static final String GET_ALL_MONITOR_PROTOCOLS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/inform-protocols");
    // Получение параметров протоколов.
    public static final String GET_PROTOCOLS_PARAMETERS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/monitor-protocol-params-keys?");
    // Получение всех статусов мониторов.
    public static final String GET_ALL_MONITOR_STATUSES = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/monitors/monitor-statuses");

// Orders controller.
    // Контекстный поиск заказов контроллера.
    public static final String SEARCH_FOR_CONTROLLERS_ORDERS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/orders?");
    // Добавление заказа контроллера.
    public static final String ADD_CONTROLLER_ORDER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/orders");
    // Получение информации о заказе.
    public static final String GET_ORDER_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/orders/");
    // Обновление информации о заказе.
    public static final String UPDATE_ORDER_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/orders/");
    // Удаляю заказ контроллера по его 'id'.
    public static final String DELETE_ORDER_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/orders/");

// Printer controller.
    // Добавление принтера агрегации.
    public static final String ADD_AGGREGATION_PRINTER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printers");
    // Контекстный поиск принтеров агрегации.
    public static final String SEARCH_FOR_AGGREGATION_PRINTERS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printers?");
    // Получить информацию о принтере по его 'id'.
    public static final String GET_PRINTER_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printers/");
    // Обновить информацию о принтере по его 'id'.
    public static final String UPDATE_PRINTER_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printers/");
    // Удаляю информацию о принтере по его 'id'.
    public static final String DELETE_PRINTER_INFO = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printers/");

// Printing controller.
    // Тест на прямую печать.
    public static final String DIRECT_PRINTING_TEST = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printing/direct-print");
    // Тест на печать по шаблону.
    public static final String TEMPLATE_PRINTING_TEST = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printing/print-by-template");
    // Создание сканера.
    public static final String CREATE_SCANNER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/scanners");
    // Создание шаблона.
    public static final String CREATE_TEMPLATE = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates");
    // Создание рабочей станции.
    public static final String CREATE_WORK_STATION = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/workstations");
    // Агрегирование по коду datamatrix.
    public static final String AGGREGATION_DATAMATRIX_CODE = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/printing/print-aggregate-by-datamatrix");

// Scanner controller.
    // Добавить сканер.
    public static final String ADD_SCANNER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/scanners");
    // Контекстный поиск сканеров.
    public static final String SEARCH_FOR_SCANNERS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/scanners?");
    // Получение информации о сканере по его 'id'.
    public static final String GET_SCANNER_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/scanners/");
    // Обновление сканнера по его 'id'.
    public static final String UPDATE_SCANNER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/scanners/");
    // Удаление сканера по его 'id'.
    public static final String DELETE_SCANNER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/scanners/");

// Print template controller.
    // Добавление шаблона печати.
    public static final String ADD_PRINT_TEMPLATE = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates");
    // Осуществление контекстного поиска шаблонов печати.
    public static final String SEARCH_FOR_PRINT_TEMPLATES = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates?");
    // Получение информации о шаблоне печати по его 'id'.
    public static final String GET_PRINT_TEMPLATE_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates/");
    // Обновление информации о шаблоне печати по его 'id'.
    public static final String UPDATE_PRINT_TEMPLATE_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates/");
    // Удаление информации о шаблоне печати по его 'id'.
    public static final String DELETE_PRINT_TEMPLATE_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates/");
    // Получение карты замены значений для печати на основании описаний.
    public static final String GET_VALUES_MAP_FOR_PRINTING = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates/placeholder");
    // Добавление или обновление шаблона, даты производства и партии у выбранных сущностей.
    public static final String ADD_OR_UPDATE_PRINT_TEMPLATE_ENTITIES = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/print-templates/add-or-update-template-and-batch-and-production-date");

// System properties controller V2.
    // Получить список системных реквизитов.
    public static final String GET_SYS_PROPS_LIST = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sys-props?");
    // Создание системного реквизита.
    public static final String CREATE_SYS_PROPERTY = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sys-props");
    // Обновление системного реквизита.
    public static final String UPDATE_SYS_PROPERTY = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sys-props/");
    // Удаление системного реквизита.
    public static final String DELETE_SYS_PROPERTY = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sys-props/");
    // Получение списка доступных параметров.
    public static final String GET_PARAMETERS_LIST = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sys-props/keys");

// Workstation controller.
    // Контекстный поиск рабочих станций.
    public static final String SEARCH_FOR_WORKSTATIONS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/workstations?");
    // Обновление рабочей станции.
    public static final String UPDATE_WORKSTATION = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/workstations/");
    // Получение информации рабочей станции по её 'id'.
    public static final String GET_WORKSTATION_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/workstations/");
    // Удаление рабочей станции по её 'id'.
    public static final String DELETE_WORKSTATION_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/workstations/");

// Clean up scheduler controller.
    // Получение всех типов расписаний.
    public static final String GET_ALL_TYPES_OF_SCHEDULERS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/all-clean-up-scheduler-types");
    // Получения всех расписаний по очисткам.
    public static final String GET_ALL_CLEAN_SCHEDULERS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/all-clean-up-schedulers");
    // Установка следующее времени срабатывания по расписанию.
    public static final String SET_NEXT_CLEAN_SCHEDULER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/check-clean-up-scheduler?");
    // Перезапуск задания для отправки сообщений в очередь.
    public static final String RESTART_TASK_TO_SEND_MASSAGE_TO_QUEUE = BASE_URI_TEST_STAGE_8088.concat("/core-printer/restart-rabbit-mq-scheduler");
    // Изменение расписания по очистке.
    public static final String CHANGE_CLEAN_SCHEDULER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/update-clean-up-scheduler");
    // Удаление расписания по очистке.
    public static final String DELETE_CLEAN_SCHEDULER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-clean-up-scheduler");

// Datamatrix controller.
    // Получение всех имеющихся сроков годности.
    public static final String GET_ALL_EXISTING_SHELF_LIFE = BASE_URI_TEST_STAGE_8088.concat("/core-printer/all-bbds");
    // Удаление всех просроченных кодов.
    public static final String DELETE_ALL_EXPIRED_CODES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-bbd-old-codes");
    // Удаление просроченных кодов в диапазоне заданных дат.
    public static final String DELETE_EXPIRED_CODES_DURING_DATES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-bbd-old-codes-in-range");
    // Удаление кодов для поискового индекса.
    public static final String DELETE_CODES_FOR_SEARCHING_INDEX = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-codes");
    // Удаление строк доступным количеством кодов равным нулю из таблицы статистики.
    public static final String DELETE_STRINGS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-finished-codes-from-tables-state");
    // Удаление отпечатанных кодов, созданных в диапазоне дат.
    public static final String DELETE_PRINTED_CODES_DURING_DATES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-printed-codes-in-range");
    // Удаление всех отпечатанных кодов.
    public static final String DELETE_ALL_PRINTED_CODES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/delete-used-codes");
    // Проверка статуса удаления задания.
    public static final String CHECK_STATUS_DELETED_TASK = BASE_URI_TEST_STAGE_8088.concat("/core-printer/deletion-task");
    // Создание индекса (предварительное условие).
    public static final String CREATE_INDEX = BASE_URI_TEST_STAGE_8088.concat("/core-printer/orders");
    // Поиск счётчика для печати кодов по индексу.
    public static final String SEARCH_FOR_INDEX = BASE_URI_TEST_STAGE_8088.concat("/core-printer/free-codes-count?");
    // Добавление устройства.
    public static final String ADD_DEVICE_FOR_PREAMBLE = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices");
    // Получение кода из пула печати.
    public static final String GET_CODE_FROM_PRINTING_POOL = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-new-code");
    // Обновление времени окончания сроков годности.
    public static final String UPDATE_CODES_EXPIRATION_TIME = BASE_URI_TEST_STAGE_8088.concat("/core-printer/update-bbd-time");
    // Проверка счётчика доступных к печати кодо.
    public static final String CHECK_AVAILABLE_CODES_COUNT = BASE_URI_TEST_STAGE_8088.concat("/core-printer/orders");
    // Загрузить коды.
    public static final String UPLOAD_CODES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/upload-codes");

// Device controller.
    // Поиск списка параметров принтера.
    public static final String SEARCH_FOR_PRINTER_PARAMETERS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-printer-params");
    // Поиск списка статусов принтера.
    public static final String SEARCH_AND_CHECK_PRINTER_STATUS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-printer-states");
    // Поиск списка всех типов принтеров.
    public static final String SEARCH_ALL_PRINTERS_TYPES_LIST = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-printers-types");
    // Запросить информацию о текущем времени сервера.
    public static final String GET_INFO_OF_CURRENT_SERVER_TIME = BASE_URI_TEST_STAGE_8088.concat("/core-printer/time");

// Device controller V2.
    // Нанесение: добавление принтера.
    public static final String ADDING_PRINTER_DEVICE_CONTROLLER_V2 = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices");
    // Получить список принтеров с пагинацией.
    public static final String GET_PRINTERS_LIST = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices?");
    // Создание устройства для парсинга его 'id'.
    public static final String CREATE_DEVICE_TO_PARSE_ID = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices");
    // Получение устройства по его 'id'.
    public static final String GET_DEVICE_BY_ID = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices/");
    // Удаление устройства по его 'id'.
    public static final String DELETE_DEVICE_BY_ID = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices/");
    // Создание заказа.
    public static final String CREATE_ORDER_DEVICE_CONTROLLER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/orders");
    // Обновление устройства.
    public static final String UPDATE_DEVICE_CONTROLLER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices/");

// Printer locks controller.
    // Получение списка установленных блокировок.
    public static final String GET_BLOCKED_SET_LIST = BASE_URI_TEST_STAGE_8088.concat("/core-printer/printer-locks?");
    // Произвести блокировку принтера.
    public static final String MAKE_PRINTER_BLOCKED = BASE_URI_TEST_STAGE_8088.concat("/core-printer/printer-locks");
    // Обновление блокировки принтера.
    public static final String UPDATE_TYPE_AND_REASON_PRINTER_BLOCKED = BASE_URI_TEST_STAGE_8088.concat("/core-printer/printer-locks/");
    // Удаление блокировки принтера.
    public static final String DELETE_PRINTER_BLOCKED = BASE_URI_TEST_STAGE_8088.concat("/core-printer/printer-locks/");

// Print job controller.
    // Создание принтера.
    public static final String CREATING_PRINTER_FOR_JOB_CONTROLLER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices");
    // Создание заказа.
    public static final String CREATING_ORDER_FOR_JOB_CONTROLLER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/orders");
    // Добавить задание на печать.
    public static final String ADD_PRINT_TASK = BASE_URI_TEST_STAGE_8088.concat("/core-printer/print-jobs");
    // Найти информацию о задании на печать по его 'id'.
    public static final String SEARCH_INFO_ABOUT_PRINT_TASK = BASE_URI_TEST_STAGE_8088.concat("/core-printer/print-jobs/");
    // Действия над заданиями: cancel - отменить, finish - завершить.
    public static final String TASKS_ACTS = BASE_URI_TEST_STAGE_8088.concat("/core-printer/print-jobs/");
    // Контекстный поиск задания.
    public static final String CONTEXT_TASK_SEARCH = BASE_URI_TEST_STAGE_8088.concat("/core-printer/print-jobs?");
    // Удалить задание на печать.
    public static final String DELETE_PRINT_TASK = BASE_URI_TEST_STAGE_8088.concat("/core-printer/print-jobs/");

// Templates controller.
    // Получить список шаблонов.
    public static final String GET_TEMPLATES_LIST = BASE_URI_TEST_STAGE_8088.concat("/core-printer/templates?");
    // Добавить принтер.
    public static final String ADD_PRINTER_TEMPLATES_CONTROLLER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/templates");
    // Получить шаблон по его 'id'.
    public static final String GET_TEMPLATE_BY_ID = BASE_URI_TEST_STAGE_8088.concat("/core-printer/templates/");
    // Обновить шаблон по его 'id'.
    public static final String UPDATE_TEMPLATE_BY_ID = BASE_URI_TEST_STAGE_8088.concat("/core-printer/templates/");
    // Удалить шаблон по его 'id'.
    public static final String DELETE_TEMPLATE_TEMPLATES_CONTROLLER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/templates/");

// Orders import to CMS.
    // Импортирование заказа в CMS.
    public static final String IMPORT_ORDER_TO_CMS = BASE_URI_TEST_STAGE_8080.concat("/core-reader/camel/order-data");

// Check production date.
    // Создание агрегационной линии.
    public static final String MAKE_AGGREGATION_LINE = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/datamatrix/create-aggregate");

// Suborders controller.
    // Создать подзаказ.
    public static final String CREATE_SUB_ORDER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sub-orders");
    // Контекстный поиск подзаказов с пагинацией.
    public static final String SEARCH_FOR_SUB_ORDERS = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sub-orders?");
    // Получение информации о подзаказе по его 'id'.
    public static final String GET_SUB_ORDER_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sub-orders/");
    // Обновление существующего подзаказа.
    public static final String UPDATE_SUB_ORDER_INFO_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sub-orders/");
    // Получить информацию о подзаказе по его 'sub_order_number'.
    public static final String GET_SUB_ORDER_INFO_BY_SUB_ORDER_NUMBER = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sub-orders/get/");
    // Удалить информацию о подзаказе по его 'id'.
    public static final String DELETE_SUB_ORDER_BY_ID = BASE_URI_TEST_STAGE_65344.concat("/core-aggregator/sub-orders/");

// Нагрузочное.
    // Добавление принтера.
    public static final String ADD_PRINTER = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices");
    // Получить список ТИПОВ существующих принтеров.
    public static final String PRINTERS_TYPES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-printers-types");
    // Загрузить коды и отправить на ручку.
    public static final String LOAD_CODES = BASE_URI_TEST_STAGE_8088.concat("/core-printer/upload-codes");
    // Обновить устройство (принтер).
    public static final String UPDATE_DEVICE = BASE_URI_TEST_STAGE_8088.concat("/core-printer/v2/devices/");
    // Получить коды по атрибуту.
    public static final String GET_CODES_BY_ATTRIBUTE = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-new-codes-by-searching-attributes");
    // Получить коды из пула печати.
    public static final String GET_CODES_FROM_PRINTING_POOL = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-new-codes");
    // Получить один код из пула для печати.
    public static final String GET_A_CODE_FROM_PRINTING_POOL = BASE_URI_TEST_STAGE_8088.concat("/core-printer/get-new-code");

//RabbitMQ.
    // Названия очередей.
    public static final String QUEUE_EXTERNAL_DATAMATRIX_CODES = "external-datamatrix-codes";
    public static final String QUEUE_TO_TOTAL_MARK_AGGREGATION = "to-total-mark-aggregation";
}
