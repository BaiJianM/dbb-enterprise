package com.gientech.iot.demo.biz.stream;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@FunctionalInterface
public interface Seq<T> {

    void consume(Consumer<T> consumer);

    static <T> void stop() {
        throw StopException.INSTANCE;
    }

    default void consumeTillStop(Consumer<T> consumer) {
        try {
            consume(consumer);
        } catch (StopException ignored) {
        }
    }

    static <T> Seq<T> unit(T t) {
        return c -> c.accept(t);
    }

    @SafeVarargs
    static <T> Seq<T> of(T... ts) {
        return Arrays.asList(ts)::forEach;
    }

    static <T> Seq<T> ofCollection(Collection<T> c) {
        return c::forEach;
    }

    default <E> Seq<E> map(Function<T, E> function) {
        Objects.requireNonNull(function);
        return c -> consume(t -> c.accept(function.apply(t)));
    }

    default <E> Seq<E> flatMap(Function<T, Seq<E>> function) {
        Objects.requireNonNull(function);
        return c -> consume(t -> function.apply(t).consume(c));
    }

    default Seq<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return c -> consume(t -> {
            if (predicate.test(t)) {
                c.accept(t);
            }
        });
    }

    default Seq<T> take(int n) {
        return c -> {
            int[] i = {n};
            consumeTillStop(t -> {
                if (i[0]-- > 0) {
                    c.accept(t);
                } else {
                    stop();
                }
            });
        };
    }

    default Seq<T> takeWhile(Predicate<T> predicate) {
        return c -> consume(t -> {
            if (predicate.test(t)) c.accept(t);
        });
    }

    default Seq<T> drop(int n) {
        return c -> {
            int[] i = {n - 1};
            consume(t -> {
                if (i[0] < 0) {
                    c.accept(t);
                } else {
                    i[0]--;
                }
            });
        };
    }

    default Seq<T> dropWhile(Predicate<T> predicate) {
        return c -> consume(t -> {
            if (!predicate.test(t)) c.accept(t);
        });
    }

    default Seq<T> onEach(Consumer<T> consumer) {
        return c -> consume(consumer.andThen(c));
    }

    default Seq<T> distinct() {
        LinkedHashSetSeq<T> setSeq = new LinkedHashSetSeq<>();
        consume(setSeq::add);
        return setSeq;
    }

    default <E> Seq<T> distinctBy(Function<T, E> mapping) {
        LinkedHashSetSeq<T> tSeq = new LinkedHashSetSeq<>();
        LinkedHashSetSeq<E> eSeq = new LinkedHashSetSeq<>();
        consume(t -> {
            E e = mapping.apply(t);
            if (!eSeq.contains(e)) tSeq.add(t);
            eSeq.add(e);
        });
        return tSeq;
    }

    default Optional<T> findFirst(Predicate<T> predicate) {
        AtomicReference<T> target = new AtomicReference<>();
        consumeTillStop(t -> {
            if (predicate.test(t)) {
                target.set(t);
                stop();
            }
        });
        return Optional.ofNullable(target.get());
    }

    default Optional<List<T>> findAny(Predicate<T> predicate) {
        List<T> list = Collections.synchronizedList(new ArrayList<>());
        consume(t -> {
            if (predicate.test(t)) list.add(t);
        });
        return Optional.of(list);
    }

    default void forEvery(Consumer<T> consumer) {
        consume(consumer);
    }

    default long count() {
        AtomicLong count = new AtomicLong(0);
        consume(t -> count.getAndIncrement());
        return count.get();
    }

    default long sum() {
        AtomicLong sum = new AtomicLong(0);
        consume(t -> {
            if (t instanceof Number) {
                sum.getAndAdd(((Number) t).longValue());
            } else {
                throw new IllegalArgumentException("非法的数值类型");
            }
        });
        return sum.get();
    }

    default Optional<T> min(Comparator<T> comparator) {
        AtomicReference<T> target = new AtomicReference<>();
        consumeTillStop(t -> {
            if (target.get() == null) target.set(t);
            if (comparator.compare(target.get(), t) > 0) {
                target.set(t);
            }
        });
        return Optional.ofNullable(target.get());
    }

    default Optional<T> max(Comparator<T> comparator) {
        AtomicReference<T> target = new AtomicReference<>();
        consumeTillStop(t -> {
            if (target.get() == null) target.set(t);
            if (comparator.compare(target.get(), t) < 0) {
                target.set(t);
            }
        });
        return Optional.ofNullable(target.get());
    }

    default boolean anyMatch(Predicate<T> predicate) {
        AtomicBoolean r = new AtomicBoolean(false);
        consumeTillStop(t -> {
            r.set(predicate.test(t));
            if (r.get()) {
                stop();
            }
        });
        return r.get();
    }

    default boolean allMatch(Predicate<T> predicate) {
        AtomicBoolean r = new AtomicBoolean(true);
        consumeTillStop(t -> {
            r.set(predicate.test(t));
            if (!r.get()) {
                stop();
            }
        });
        return r.get();
    }

    default boolean nonMatch(Predicate<T> predicate) {
        AtomicBoolean r = new AtomicBoolean(false);
        consumeTillStop(t -> {
            r.set(predicate.test(t));
            if (r.get()) {
                stop();
            }
        });
        return !r.get();
    }

    default List<T> toList() {
        List<T> list = new ArrayList<>();
        consume(list::add);
        return list;
    }

    // default T[] toArray() {
    //     ArraySeq<T> arraySeq = new ArraySeq<>();
    //     consume(arraySeq::add);
    //
    // }

    default Set<T> toSet() {
        Set<T> set = new HashSet<>();
        consume(set::add);
        return set;
    }

    default <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                   Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        Map<K, V> m = new HashMap<>(16);
        consume(t -> m.put(keyMapper.apply(t), valueMapper.apply(t)));
        return m;
    }

    default <K, V, M extends Map<K, V>> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                                        Function<? super T, ? extends V> valueMapper,
                                                        Supplier<M> mapSupplier) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        M m = mapSupplier.get();
        consume(t -> m.put(keyMapper.apply(t), valueMapper.apply(t)));
        return m;
    }

    default <K, V> Map<K, V> toConcurrentMap(Function<? super T, ? extends K> keyMapper,
                                             Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        Map<K, V> m = new ConcurrentHashMap<>(16);
        consume(t -> m.put(keyMapper.apply(t), valueMapper.apply(t)));
        return m;
    }

    default <K, V> Map<K, List<T>> groupingBy(Function<? super T, ? extends K> classifier) {
        Objects.requireNonNull(classifier);
        Map<K, List<T>> m = new HashMap<>(16);
        consume(t -> {
            K key = classifier.apply(t);
            List<T> valList = m.get(key) == null ? new ArrayList<>() : m.get(key);
            valList.add(t);
            m.put(key, valList);
        });
        return m;
    }

    default <K, V extends Collection<T>> Map<K, V> groupingBy(Function<? super T, ? extends K> classifier,
                                                              Supplier<V> downstream) {
        Objects.requireNonNull(classifier);
        Map<K, V> m = new HashMap<>(16);
        consume(t -> {
            K key = classifier.apply(t);
            V val = downstream.get();
            if (val instanceof SortedSet) {
                if (!(t instanceof Comparable)) {
                    throw new ClassCastException("集合中的元素需实现Comparable接口");
                }
            }
            Collection<T> c = m.get(key) == null ? new ArrayList<>() : m.get(key);
            val.addAll(c);
            val.add(t);
            m.put(key, val);
        });
        return m;
    }

    /**
     * 由于这里的Iterator本质已经发生了改变，这种操作也会有一些限制，没法再使用parallel方法将其转为并发流，也不能用limit方法限制数量。
     * 不过除此以外，像map, filter, flatMap, forEach, collect等等方法，只要不涉及流的中断，都可以正常使用
     *
     * @param seq
     * @param <T>
     * @return
     */
    static <T> Stream<T> stream(Seq<T> seq) {
        Iterator<T> iterator = new Iterator<T>() {
            @Override
            public boolean hasNext() {
                throw new NoSuchElementException();
            }

            @Override
            public T next() {
                throw new NoSuchElementException();
            }

            @Override
            public void forEachRemaining(Consumer<? super T> action) {
                seq.consume(action::accept);
            }
        };
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
                false);
    }

    default Seq<T> sorted(Comparator<T> comparator) {
        ArraySeq<T> arraySeq = new ArraySeq<>();
        consume(arraySeq::add);
        arraySeq.sort(comparator);
        return arraySeq;
    }

    @SuppressWarnings("unchecked")
    default Seq<T> sorted() {
        ArraySeq<T> arraySeq = new ArraySeq<>();
        consume(arraySeq::add);
        arraySeq.sort((Comparator<? super T>) Comparator.naturalOrder());
        return arraySeq;
    }

    default <E, R> Seq<R> zip(Iterable<E> iterable, BiFunction<T, E, R> function) {
        return c -> {
            Iterator<E> iterator = iterable.iterator();
            consumeTillStop(t -> {
                if (iterator.hasNext()) {
                    c.accept(function.apply(t, iterator.next()));
                } else {
                    stop();
                }
            });
        };
    }

    default Seq<T> parallel() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return c -> map(t -> pool.submit(() -> c.accept(t))).cache().consume(ForkJoinTask::join);
    }

    default void asyncConsume(Consumer<T> consumer) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        map(t -> pool.submit(() -> consumer.accept(t))).cache().consume(ForkJoinTask::join);
    }

    default Seq<T> cache() {
        ArraySeq<T> arraySeq = new ArraySeq<>();
        consume(arraySeq::add);
        return arraySeq;
    }

    static Seq<Object> ofJson(Object node) {
        return Seq.ofTree(node, n -> c -> {
            if (n instanceof Iterable) {
                ((Iterable<?>) n).forEach(c);
            } else if (n instanceof Map) {
                ((Map<?, ?>) n).values().forEach(c);
            }
        });
    }

    static Seq<Node> scanTree(Node node) {
        return ofTree(node, n -> Seq.of(n.left, n.right));
    }

    static <N> Seq<N> ofTree(N node, Function<N, Seq<N>> sub) {
        return c -> scanTree(c, node, sub);
    }

    static <N> void scanTree(Consumer<N> c, N node, Function<N, Seq<N>> sub) {
        c.accept(node);
        sub.apply(node).consume(n -> {
            if (n != null) {
                scanTree(c, n, sub);
            }
        });
    }

    default String join(String sep) {
        Objects.requireNonNull(sep);
        StringJoiner joiner = new StringJoiner(sep);
        consume(t -> joiner.add(t.toString()));
        return joiner.toString();
    }

    default Seq<String> readLines(File file) {
        return c -> {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
                String s;
                while ((s = reader.readLine()) != null) {
                    c.accept(s);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <T> Seq<T> readExcel(String pathName, Class<T> head) {
        return c -> {
            ReadListener<T> listener = new ReadListener<T>() {
                @Override
                public void invoke(T data, AnalysisContext context) {
                    c.accept(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            };
            EasyExcel.read(pathName, head, listener).sheet().doRead();
        };
    }
}
