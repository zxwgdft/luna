package com.luna.his.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 处理数据表中一个字段存储关联表多个主键的情况下的一些通用处理
 *
 * @author TontoZhou
 */
public class MultiIdUtil {

    public static int[] split2Int(String ids) {
        if (ids != null && ids.length() > 0) {
            String[] arr = ids.split(",");
            if (arr[0].length() == 0) {
                int[] idArr = new int[arr.length - 1];
                for (int i = 1; i < arr.length; i++) {
                    idArr[i - 1] = Integer.parseInt(arr[i]);
                }
                return idArr;
            } else {
                int[] idArr = new int[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    idArr[i] = Integer.parseInt(arr[i]);
                }
                return idArr;
            }
        }
        return new int[0];
    }

    public static List<Integer> split2IntList(String ids) {
        if (ids != null && ids.length() > 0) {
            String[] arr = ids.split(",");
            if (arr[0].length() == 0) {
                List<Integer> idArr = new ArrayList(arr.length - 1);
                for (int i = 1; i < arr.length; i++) {
                    idArr.add(Integer.parseInt(arr[i]));
                }
                return idArr;
            } else {
                List<Integer> idArr = new ArrayList(arr.length);
                for (int i = 0; i < arr.length; i++) {
                    idArr.add(Integer.parseInt(arr[i]));
                }
                return idArr;
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static long[] split2Long(String ids) {
        if (ids != null && ids.length() > 0) {
            String[] arr = ids.split(",");
            if (arr[0].length() == 0) {
                long[] idArr = new long[arr.length - 1];
                for (int i = 1; i < arr.length; i++) {
                    idArr[i - 1] = Long.parseLong(arr[i]);
                }
                return idArr;
            } else {
                long[] idArr = new long[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    idArr[i] = Long.parseLong(arr[i]);
                }
                return idArr;
            }
        }
        return new long[0];
    }

    public static List<Long> split2LongList(String ids) {
        if (ids != null && ids.length() > 0) {
            String[] arr = ids.split(",");
            if (arr[0].length() == 0) {
                List<Long> idArr = new ArrayList(arr.length - 1);
                for (int i = 1; i < arr.length; i++) {
                    idArr.add(Long.parseLong(arr[i]));
                }
                return idArr;
            } else {
                List<Long> idArr = new ArrayList(arr.length);
                for (int i = 0; i < arr.length; i++) {
                    idArr.add(Long.parseLong(arr[i]));
                }
                return idArr;
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static String joinId2Str(Collection ids) {
        if (ids != null && ids.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(',');
            for (Object id : ids) {
                sb.append(id.toString()).append(',');
            }
            return sb.toString();
        }
        return "";
    }

    public static String joinId2Str(long[] ids) {
        if (ids != null && ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(',');
            for (long id : ids) {
                sb.append(id).append(',');
            }
            return sb.toString();
        }
        return "";
    }

    public static String joinId2Str(int[] ids) {
        if (ids != null && ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(',');
            for (int id : ids) {
                sb.append(id).append(',');
            }
            return sb.toString();
        }
        return "";
    }

    public static String joinId2Str(String[] ids) {
        if (ids != null && ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(',');
            for (String id : ids) {
                sb.append(id).append(',');
            }
            return sb.toString();
        }
        return "";
    }

    public static String getLikeSql(Long id) {
        return "," + id + ",";
    }

    public static String getLikeSql(Integer id) {
        return "," + id + ",";
    }


}
