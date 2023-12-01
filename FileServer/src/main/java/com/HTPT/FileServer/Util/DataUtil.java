package com.HTPT.FileServer.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class DataUtil {
    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        }
        if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        }
        return false;
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static <T> T passDBDataToObject(T object, ResultSet resultSet, Class<T> clazz, Logger log) {
        Field[] fields = clazz.getDeclaredFields();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                columnNames.add(resultSetMetaData.getColumnName(i).toLowerCase());
            }
            for (Field field : fields) {
                field.setAccessible(true);
                if (columnNames.contains(field.getName().toLowerCase())) {
                    if (field.getType().equals(Integer.class)) {
                        field.set(object, resultSet.getInt(field.getName()));
                    } else if (field.getType().equals(Long.class)) {
                        field.set(object, resultSet.getLong(field.getName()));
                    } else if (field.getType().equals(BigDecimal.class)) {
                        field.set(object, resultSet.getBigDecimal(field.getName()));
                    } else if (field.getType().equals(String.class)) {
                        field.set(object, resultSet.getString(field.getName()) == null ? null : resultSet.getString(field.getName()).trim());
                    } else if (field.getType().equals(Date.class)) {
                        field.set(object, resultSet.getDate(field.getName()));
                    } else if (field.getType().equals(Timestamp.class)) {
                        field.set(object, resultSet.getTimestamp(field.getName()));
                    } else if (field.getType().equals(Boolean.class)) {
                        field.set(object, resultSet.getBoolean(field.getName()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error when getting data column from db");
            throw new RuntimeException(e);
        }
        return object;
    }

    public static String convertToLikeConditional(String conditional) {
        conditional = conditional.replace("%", "\\%").replace("_", "\\_");

        return "%" + conditional + "%";
    }

    public static String date2StringByPattern(Date date, String pattern) throws ParseException {
        if (date == null || DataUtil.isNullOrEmpty(pattern)) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }

        return obj1.toString().trim();
    }

    public static String safeToString(Object obj1) {
        return safeToString(obj1, "");
    }

    public static Date safeToDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (Date) obj1;
    }

    public static Long safeToLong(Object obj1, Long defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        if (obj1 instanceof BigDecimal) {
            return ((BigDecimal) obj1).longValue();
        }
        if (obj1 instanceof BigInteger) {
            return ((BigInteger) obj1).longValue();
        }
        if (obj1 instanceof Double) {
            return ((Double) obj1).longValue();
        }

        try {
            return Long.parseLong(obj1.toString());
        } catch (final NumberFormatException nfe) {
//            log.error(nfe.getMessage(),nfe);
            return defaultValue;
        }
    }

    public static <T> List<T> safeToList(List<T> lst) {
        if (CollectionUtils.isEmpty(lst)) {
            return new ArrayList<T>();
        }
        return lst;
    }

    /**
     * @param obj1 Object
     * @return Long
     */
    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    public static Double safeToDouble(Object obj1, Double defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj1.toString());
        } catch (final NumberFormatException nfe) {
//            log.error(nfe.getMessage(),nfe);
            return defaultValue;
        }
    }

    public static BigDecimal safeToBigDecimal(Object obj1) {
        if (obj1 == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(obj1.toString());
        } catch (final NumberFormatException nfe) {
//            log.error(nfe.getMessage(),nfe);
            return BigDecimal.ZERO;
        }
    }

    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    public static BigInteger safeToBigInteger(Object obj1, BigInteger defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return new BigInteger(obj1.toString());
        } catch (final NumberFormatException nfe) {
//            log.error(nfe.getMessage(),nfe);
            return defaultValue;
        }
    }

    public static BigInteger safeToBigInteger(Object obj1) {
        return safeToBigInteger(obj1, BigInteger.ZERO);
    }

    public static int safeToInt(Object obj1, int defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(obj1.toString());
        } catch (final NumberFormatException nfe) {
//            log.error(nfe.getMessage(),nfe);
            return defaultValue;
        }
    }

    /**
     * @param obj1 Object
     * @return int
     */
    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }


    public static <T> List<T> getResultFromListObjects(List<Object[]> listObjects, String classPath) {
        try {
            List<T> result = new ArrayList<>();

            for (Object[] objects : listObjects) {
                Class<?> c = Class.forName(classPath);
                Constructor<?> cons = c.getConstructor();
                Object object = cons.newInstance();

                Field[] fields = object.getClass().getDeclaredFields();

                String dateFormat = "dd-MM-yyyy";

                for (int i = 0; i < fields.length; i++) {
                    if (i > objects.length - 1) break;

                    Field f = fields[i];

                    f.setAccessible(true);
                    Class t = f.getType();

                    Object item = objects[i];
                    if (item == null) continue;
                    if (String.class.getName().equalsIgnoreCase(t.getName())) {
                        if (item instanceof String || item instanceof Long || item instanceof Character) {
                            f.set(object, DataUtil.safeToString(item));
                        } else if (item instanceof java.sql.Date || item instanceof Date
                                || item instanceof Timestamp
                        ) {
                            f.set(object, date2StringByPattern(DataUtil.safeToDate(item), dateFormat));
                        }
                    } else if (Long.class.getName().equalsIgnoreCase(t.getName()) || long.class.getName().equalsIgnoreCase(t.getName())) {
                        f.set(object, DataUtil.safeToLong(item));
                    } else if (Double.class.getName().equalsIgnoreCase(t.getName()) || double.class.getName().equalsIgnoreCase(t.getName())) {
                        f.set(object, DataUtil.safeToDouble(item));
                    } else if (Boolean.class.getName().equalsIgnoreCase(t.getName()) || boolean.class.getName().equalsIgnoreCase(t.getName())) {
                        f.set(object, "true".equalsIgnoreCase(DataUtil.safeToString(item)));
                    } else if (Date.class.getName().equalsIgnoreCase(t.getName())) {
                        f.set(object, DataUtil.safeToDate(item));
                    } else if (BigDecimal.class.getName().equalsIgnoreCase(t.getName())) {
                        f.set(object, DataUtil.safeToBigDecimal(item));
                    } else if (Integer.class.getName().equalsIgnoreCase(t.getName())) {
                        f.set(object, DataUtil.safeToInt(item));
                    }
                }


                result.add((T) object);
            }

            return result;
        } catch (Exception e) {
//            log.error(e.getMessage(),e);
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static Date stringToDate(String value) {
        return string2DateByPattern(value, "dd/MM/yyyy");
    }

    public static Date string2DateByPattern(String value, String pattern) {
        if (!DataUtil.isNullOrEmpty(value)) {
            SimpleDateFormat dateTime = new SimpleDateFormat(pattern);
            dateTime.setLenient(false);
            try {
                return dateTime.parse(value);
            } catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    public static String dateToStringddMMyyyy(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            return date.format(value);
        }
        return "";
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget) {
        return convertFromQueryResult(listSource, classTarget, Constant.DATETIME_FORMAT1);
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget, String dateFormat) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);

        return convertFromQueryResult(listSource, classTarget, dateFormats);
    }

    public static <T> List<T> convertFromQueryResult(List<Tuple> listSource, Class<T> classTarget, Map<String, String> dateFormats) {
        try {
            List<T> result = new ArrayList<>();

            for (Tuple sourceItem : listSource) {
                Constructor<?> cons = classTarget.getConstructor();
                Object target = cons.newInstance();

                Field[] targetFields = target.getClass().getDeclaredFields();


                for (Field targetFieldItem : targetFields) {
                    setFieldData(sourceItem, targetFieldItem, target, dateFormats);
                }

                result.add((T) target);
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new ArrayList<>();
    }

    public static <T> T convertFromQueryResult(Tuple source, Class<T> classTarget, Map<String, String> dateFormats) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();

            Field[] targetFields = target.getClass().getDeclaredFields();


            for (Field targetFieldItem : targetFields) {
                setFieldData(source, targetFieldItem, target, dateFormats);
            }

            return ((T) target);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T convertFromQueryResult(Tuple source, Class<T> classTarget, String dateFormat) {
        Map<String, String> dateFormats = new HashMap<>();
        dateFormats.put("ALL", dateFormat);

        return convertFromQueryResult(source, classTarget, dateFormats);
    }

    public static <T> T convertFromQueryResult(Tuple source, Class<T> classTarget) {
        return convertFromQueryResult(source, classTarget, Constant.DATETIME_FORMAT1);
    }

    private static void setFieldData(
            Tuple sourceItem, Field targetFieldItem, Object target, Map<String, String> dateFormats
    ) throws IllegalAccessException, ParseException {
        List<TupleElement<?>> sourceFields = sourceItem.getElements();

        String fieldName = targetFieldItem.getName().toLowerCase();

        targetFieldItem.setAccessible(true);
        Class<?> targetFieldType = targetFieldItem.getType();
        String targetFieldTypeName = targetFieldType.getName();

        for (TupleElement<?> sourceFieldItem : sourceFields) {
            String sourceFieldName = sourceFieldItem.getAlias();
            String sourceFieldNameRemoveUnderscore = sourceFieldItem.getAlias().replace("_", "");

            if (!fieldName.equalsIgnoreCase(sourceFieldNameRemoveUnderscore)) continue;

            String dateFormat;
            if (dateFormats.size() == 1) {
                dateFormat = dateFormats.get("ALL");
            } else {
                dateFormat = dateFormats.get(sourceFieldName);
            }

            Object sourceItemData = sourceItem.get(sourceFieldName);
            if (sourceItemData == null) continue;
            if (String.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                if (sourceItemData instanceof String || sourceItemData instanceof Long || sourceItemData instanceof Character || sourceItemData instanceof Integer) {
                    targetFieldItem.set(target, safeToString(sourceItemData));
                } else if (sourceItemData instanceof Date || sourceItemData instanceof Timestamp) {
                    targetFieldItem.set(target, date2StringByPattern(safeToDate(sourceItemData), dateFormat));
                }
            } else if (Long.class.getName().equalsIgnoreCase(targetFieldTypeName) || long.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, safeToLong(sourceItemData, null));
            } else if (Double.class.getName().equalsIgnoreCase(targetFieldTypeName) || double.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, Objects.requireNonNullElse(sourceItemData, null));
            } else if (Boolean.class.getName().equalsIgnoreCase(targetFieldTypeName) || boolean.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, "true".equalsIgnoreCase(safeToString(sourceItemData)));
            } else if (Date.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, Objects.requireNonNullElse(sourceItemData, null));
            } else if (BigDecimal.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                BigDecimal value = BigDecimal.ZERO;
                if (!Objects.isNull(sourceItemData)){
                    if (sourceItemData instanceof Double)
                        value = new BigDecimal((double) sourceItemData);
                    else if (sourceItemData instanceof Float)
                        value = new BigDecimal((float) sourceItemData);
                    else if (sourceItemData instanceof Integer)
                        value = new BigDecimal((int) sourceItemData);
                    else if (sourceItemData instanceof Long)
                        value = new BigDecimal((long) sourceItemData);
                    else if (sourceItemData instanceof BigDecimal)
                        value = (BigDecimal) sourceItemData;
                }
                targetFieldItem.set(target, value);
            } else if (Integer.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                targetFieldItem.set(target, Objects.requireNonNullElse((sourceItemData instanceof Short) ? Integer.valueOf((Short) sourceItemData) :  sourceItemData, null));
            } else if (Short.class.getName().equalsIgnoreCase(targetFieldTypeName)) {
                Short value = (Short) sourceItemData;
                targetFieldItem.set(target, Objects.requireNonNullElse(Integer.valueOf(value), null));
            } else if (List.class.getName().equals(targetFieldTypeName)){
                targetFieldItem.set(target, sourceItemData);
            }
        }
    }

    public static Boolean safeToBool(Object obj1) {
        if (obj1 == null) {
            return false;
        }
        return (Boolean) obj1;
    }

    public static Boolean safeToBool(Object obj1, Boolean defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        return (Boolean) obj1;
    }

    public static String safeDate2StringByPattern(Date date) {
        String pattern = Constant.DATETIME_FORMAT1;
        try {
            if (date == null || StringUtils.isBlank(pattern)) {
                return null;
            }

            DateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static String queryLike(String target) {
        if (StringUtils.isBlank(target))
            return null;
        return "%" + target.trim() + "%";
    }

    public static Map<String, Object> from(Tuple tuple, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Map<String, Object> rs  = new HashMap<>() ;
        tuple.getElements().forEach(element -> {
            String alias = element.getAlias();
            Object value = tuple.get(element);
            if (value instanceof Date)
                rs.put(alias, sdf.format(tuple.get(element)));
            else
                rs.put(alias, tuple.get(element));
        });
        return rs;
    }

    private static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static String dateTimeToString(Date value) {
        if (value != null) {
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return date.format(value);
        }
        return "";
    }
    public static String extractJson(String language, String jsonData) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Map<String, String> map = om.readValue(jsonData, Map.class);
        String result = map.get(language);
        return result;
    }
    public static Object castToNullIfEmptyOrNull(Object target){
        if (isNullObject(target)){
            return null;
        }
        else return target;
    }
}
