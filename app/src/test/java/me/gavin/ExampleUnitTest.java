package me.gavin;

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void reference() {
        String abc = new String("abc");
        WeakReference<String> abcWeakRef = new WeakReference<>(abc);
        m(abcWeakRef.get());
        String z = abcWeakRef.get();
        abc = null;
        System.out.println("before gc: " + abcWeakRef.get());
        System.gc();
        System.out.println("after gc: " + abcWeakRef.get());
    }

    private void m(String s) {
        String str = s;
        System.out.println("??? : " + str);
    }

    @Test
    public void integer() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE + 1);
        System.out.println(Integer.MAX_VALUE + 2);
    }

    @Test
    public void soft() {
        ArrayList<String> newArray = new ArrayList<>();
        newArray.add("汽车");
        newArray.add("ab12");
        newArray.add("ab21");
        newArray.add("公安");
        newArray.add("怡");
        newArray.add("张新");
        newArray.add("广州");
        newArray.add("test");
        newArray.add("pp");
        newArray.add("？23");
        newArray.add(".23");

        Collator col = Collator.getInstance(Locale.CHINESE);
        Collections.sort(newArray, col);

        for (String i : newArray) {
            System.out.print(i + " ");
        }
    }

    @Test
    public void soft2() {
        ArrayList<Long> list = new ArrayList<>();
        list.add(0L);
        list.add(100L);
        list.add(30L);
        list.add(50L);
        Collections.sort(list, (o1, o2) -> o1 > o2 ? -1 : 1);
        for (Long t : list) {
            System.out.println(t);
        }
    }

    @Test
    public void test2() {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            System.out.println("subscribing");
            e.onError(new RuntimeException("always fails"));
        }).retryWhen(throwableObservable -> {
            //这里可以发送新的被观察者 Observable
            return throwableObservable.delay(2, TimeUnit.SECONDS).map(throwable -> 1);
        }).subscribe(System.out::println);
    }

    @Test
    public void matches() {
        String regex = ".*\\?jrs=\\d+x\\d+.*";
        String text = "http://im01.ymm.cn/msg/img/2018-03-10Y0579/1520664906520Y0579.png?jrs=100X100?x-oss-process=image/resize,s_200";
        System.out.println(text.matches(regex));
    }

    @Test
    public void timeFormat() {
        Date date = new Date();
        //b的使用，月份简称
        String str = String.format(Locale.US, "英文月份简称：%tb", date);
        System.out.println(str);
        System.out.printf("本地月份简称：%tb%n", date);
        //B的使用，月份全称
        str = String.format(Locale.US, "英文月份全称：%tB", date);
        System.out.println(str);
        System.out.printf("本地月份全称：%tB%n", date);
        //a的使用，星期简称
        str = String.format(Locale.US, "英文星期的简称：%ta", date);
        System.out.println(str);
        //A的使用，星期全称
        System.out.printf("本地星期的简称：%tA%n", date);
        //C的使用，年前两位
        System.out.printf("年的前两位数字（不足两位前面补0）：%tC%n", date);
        //y的使用，年后两位
        System.out.printf("年的后两位数字（不足两位前面补0）：%ty%n", date);
        //j的使用，一年的天数
        System.out.printf("一年中的天数（即年的第几天）：%tj%n", date);
        //m的使用，月份
        System.out.printf("两位数字的月份（不足两位前面补0）：%tm%n", date);
        //d的使用，日（二位，不够补零）
        System.out.printf("两位数字的日（不足两位前面补0）：%td%n", date);
        //e的使用，日（一位不补零）
        System.out.printf("月份的日（前面不补0）：%te%n", date);


        long millis = 6366000L;
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = millis % TimeUnit.DAYS.toMillis(1) / TimeUnit.HOURS.toMillis(1);
        long minutes = millis % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
        long seconds = millis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        String s = String.format(Locale.getDefault(), "%02d %02d %02d %02d", days, hours, minutes, seconds);
        System.out.println(s);

        String ss = String.format(Locale.getDefault(), "%1$tY.%1$tm.%1$td  %1$tR至%2$tR", date, date);
        System.out.println(ss);
    }
}