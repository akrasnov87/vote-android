package ru.mobnius.vote.data.manager.synchronization.utils;

/**
 * Результат выволнения операции
 */
public class PackageResult {
    /**
     * состояние выполнения
     */
    public boolean success;
    /**
     * тестовое сообщение
     */
    public String message;
    /**
     * Результат
     */
    private Object result;

    /**
     * Положительный результат
     * @param result Результат
     * @return объект
     */
    public static PackageResult success(Object result){
        PackageResult packageResult = new PackageResult();
        packageResult.success = true;
        packageResult.result = result;
        packageResult.message = "";
        return packageResult;
    }

    /**
     * Отрицательный результат
     * @param message Текстовое сообщение
     * @param e исключение
     * @return результат
     */
    public static PackageResult fail(String message, Exception e){
        PackageResult packageResult = new PackageResult();
        packageResult.success = false;
        packageResult.message = message;
        packageResult.result = e;
        return packageResult;
    }
}
