export class AlertMessage {
    private static idCounter = 0;

    public static CATEGORY_INFO = 'info';
    public static CATEGORY_SUCCESS = 'success';
    public static CATEGORY_ERROR = 'error';
    public static CATEGORY_WARNING = 'warning';
    public static CATEGORY_DEFAULT = 'default';

    public id: number;
    public message: string;
    public category: string;

    constructor(message: string, category: string) {
        this.message = message;
        this.category = category;
        this.id = ++AlertMessage.idCounter;
    }
}