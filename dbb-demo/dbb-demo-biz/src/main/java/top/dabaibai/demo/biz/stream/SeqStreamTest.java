package top.dabaibai.demo.biz.stream;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/12 13:57
 */
@Slf4j
public class SeqStreamTest {

    public static long test() {
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        list.add("马六");

        List<User> listUser = new ArrayList<>();

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();

        user1.setId(2);
        user1.setName("张三");

        user2.setId(2);
        user2.setName("李四");

        user3.setId(3);
        user3.setName("王五");

        user4.setId(4);
        user4.setName("马六");

        listUser.add(user1);
        listUser.add(user2);
        listUser.add(user3);
        listUser.add(user4);

        // for (int i = 5; i < 1000000; i++) {
        //     User user666 = new User();
        //     user666.setId(i);
        //     user666.setName("马六" + i);
        //     listUser.add(user666);
        // }
        //
        // listUser.stream().limit(3).forEach(System.out::println);
        return 0L;
    }

    @SuppressWarnings("all")
    static String underscoreToCamel(String str) {
        // Java没有首字母大写方法，随便现写一个
        UnaryOperator<String> capitalize = s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        // 利用生成器构造一个方法的流
        Seq<UnaryOperator<String>> seq = c -> {
            // yield第一个小写函数
            c.accept(String::toLowerCase);
            // 这里IDEA会告警，提示死循环风险，无视即可
            while (true) {
                // 按需yield首字母大写函数
                c.accept(capitalize);
            }
        };
        List<String> split = Arrays.asList(str.split("_"));
        // 这里的zip和join都在上文给出了实现
        return seq.zip(split, Function::apply).join("");
    }

    @Data
    static class User implements Comparable<User> {
        private String name;
        private Integer id;

        @Override
        public int compareTo(@NotNull User o) {
            return o.getId() - this.id;
        }
    }

    public static void main(String[] args) {
        test();
    }

}
