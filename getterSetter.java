//=========this class is one folder back to below two classes===========
package pkgName.entity;

import pkgName.GetterSetterTest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ModelUnitTest extends GetterSetterTest {
    Set<Class<?>> allClassesSet = new HashSet<>();

    @Override
    protected Set<Class<?>> getInstance() {

        // create scanner and disable default filters (that is the 'false' argument)
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        Pattern patternWithSpec = Pattern.compile(".*" + "Spec");
        Pattern patternwithTest = Pattern.compile(".*" + "Test");
        // add include filters which matches all the classes (or use your own)
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        provider.addExcludeFilter(new RegexPatternTypeFilter(patternWithSpec));
        provider.addExcludeFilter(new RegexPatternTypeFilter(patternwithTest));
        // get matching classes defined in the package


        Set<BeanDefinition> classes = provider.findCandidateComponents("pkgName.crewflip");
       // Set<BeanDefinition> entityClasses = provider.findCandidateComponents("pkgName.entity");
        Set<BeanDefinition> otherClasses = provider.findCandidateComponents("pkgName.pool");
        addClasses(classes);
       // addClasses(entityClasses);
        addClasses(otherClasses);
        return allClassesSet;

    }

    public void addClasses(Set<BeanDefinition> classes) {
        // this is how you can load the class type from BeanDefinition instance
        for (BeanDefinition bean : classes) {
            try {
                allClassesSet.add(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}

//------------------------
package pkgName; 
import java.lang.reflect.Method;

/**
 * A utility class which holds a related getter and setter method.
 */
public class GetterSetterPair {
    /** The get method. */
    private Method getter;

    /** The set method. */
    private Method setter;

    /**
     * Returns the get method.
     *
     * @return The get method.
     */
    public Method getGetter() {
        return getter;
    }

    /**
     * Returns the set method.
     *
     * @return The set method.
     */
    public Method getSetter() {
        return setter;
    }

    /**
     * Returns if this has a getter and setting method set.
     *
     * @return If this has a getter and setting method set.
     */
    public boolean hasGetterAndSetter() {
        return this.getter != null && this.setter != null;
    }

    /**
     * Sets the get Method.
     *
     * @param getter The get Method.
     */
    public void setGetter(Method getter) {
        this.getter = getter;
    }

    /**
     * Sets the set Method.
     *
     * @param setter The set Method.
     */
    public void setSetter(Method setter) {
        this.setter = setter;
    }
}
//-----------------------------

package pkgName;

import pkgName.PaginatedResult;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;


public abstract class GetterSetterTest {

    /**
     * A map of default mappers for common objects.
     */
    private static final ImmutableMap<Class<?>, Supplier<?>> DEFAULT_MAPPERS;

    static {
        final Builder<Class<?>, Supplier<?>> mapperBuilder = ImmutableMap.builder();

        /* Primitives */
        mapperBuilder.put(int.class, () -> 0);
        mapperBuilder.put(double.class, () -> 0.0d);
        mapperBuilder.put(float.class, () -> 0.0f);
        mapperBuilder.put(long.class, () -> 0l);
        mapperBuilder.put(boolean.class, () -> true);
        mapperBuilder.put(short.class, () -> (short) 0);
        mapperBuilder.put(byte.class, () -> (byte) 0);
        mapperBuilder.put(LocalDate.class, () -> (LocalDate.now()));
        mapperBuilder.put(LocalTime.class, () -> (LocalTime.now()));
        mapperBuilder.put(char.class, () -> (char) 0);
        mapperBuilder.put(String.class, () -> "ABC");
        mapperBuilder.put(Integer.class, () -> Integer.valueOf(0));
        mapperBuilder.put(Double.class, () -> Double.valueOf(0.0));
        mapperBuilder.put(Float.class, () -> Float.valueOf(0.0f));
        mapperBuilder.put(Long.class, () -> Long.valueOf(0));
        mapperBuilder.put(BigInteger.class, () -> new BigInteger("10"));
        mapperBuilder.put(Boolean.class, () -> Boolean.TRUE);
        mapperBuilder.put(Short.class, () -> Short.valueOf((short) 0));
        mapperBuilder.put(Byte.class, () -> Byte.valueOf((byte) 0));
        mapperBuilder.put(Character.class, () -> Character.valueOf((char) 0));

        mapperBuilder.put(BigDecimal.class, () -> BigDecimal.ONE);
        mapperBuilder.put(Date.class, () -> new Date());

        /* Collection Types. */
        mapperBuilder.put(Set.class, () -> Collections.emptySet());
        mapperBuilder.put(SortedSet.class, () -> Collections.emptySortedSet());
        mapperBuilder.put(List.class, () -> Collections.emptyList());
        mapperBuilder.put(Map.class, () -> Collections.emptyMap());
        mapperBuilder.put(SortedMap.class, () -> Collections.emptySortedMap());
        mapperBuilder.put(Time.class, () -> new Time(123456789998l));
        mapperBuilder.put(java.sql.Date.class, () -> new java.sql.Date(123456789998l));
        mapperBuilder.put(Timestamp.class, () -> new Timestamp(System.currentTimeMillis()));
       // mapperBuilder.put(Bean.class, () -> Bean.builder());
        mapperBuilder.put(PaginatedResult.class,() -> new PaginatedResult<>(new ArrayList<>(), 1, 1, 1));
        mapperBuilder.put(Blob.class,()-> new Blob() {
            @Override
            public long length() throws SQLException {
                return 3;
            }

            @Override
            public byte[] getBytes(long pos, int length) throws SQLException {
                return new byte[0];
            }

            @Override
            public InputStream getBinaryStream() throws SQLException {
                return null;
            }

            @Override
            public long position(byte[] pattern, long start) throws SQLException {
                return 0;
            }

            @Override
            public long position(Blob pattern, long start) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long pos, byte[] bytes) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
                return 0;
            }

            @Override
            public OutputStream setBinaryStream(long pos) throws SQLException {
                return null;
            }

            @Override
            public void truncate(long len) throws SQLException {

            }

            @Override
            public void free() throws SQLException {

            }

            @Override
            public InputStream getBinaryStream(long pos, long length) throws SQLException {
                return null;
            }
        });
        DEFAULT_MAPPERS = mapperBuilder.build();
    }

    /**
     * The get fields to ignore and not try to test.
     */
    private final Set<String> ignoredGetFields;

    /**
     * A custom mapper. Normally used when the test class has abstract objects.
     */
    private final ImmutableMap<Class<?>, Supplier<?>> mappers;

    /**
     * Creates an instance of  GetterSetterTest with the default ignore fields.
     */
    protected GetterSetterTest() {
        this(null, null);
    }

    /**
     * Creates an instance of  GetterSetterTest with ignore fields and additional custom mappers.
     */
    protected GetterSetterTest(Map<Class<?>,Supplier<?>> customMappers, Set<String> ignoreFields) {
        this.ignoredGetFields = new HashSet<>();
        if (ignoreFields != null) {
            this.ignoredGetFields.addAll(ignoreFields);
        }
        this.ignoredGetFields.add("getClass");
        this.ignoredGetFields.add("getProgram_date");
        this.ignoredGetFields.add("getdt");
        this.ignoredGetFields.add("gettm");
        this.ignoredGetFields.add("getCall");
        this.ignoredGetFields.add("gettime");
        this.ignoredGetFields.add("getBatchJobStrtTm");
        this.ignoredGetFields.add("getWorkComments");
        this.ignoredGetFields.add("geteBatchJobStrtDt");
        this.ignoredGetFields.add("beanBuilder");

        
        this.ignoredGetFields.add("responseBuilder");
        this.ignoredGetFields.add("requestBeanBuilder");
        

        if (customMappers == null) {
            this.mappers = DEFAULT_MAPPERS;
        } else {
            final Builder<Class<?>, Supplier<?>> builder = ImmutableMap.builder();
            builder.putAll(customMappers);
            builder.putAll(DEFAULT_MAPPERS);
            this.mappers = builder.build();
        }
    }

    /**
     * Calls a getter and verifies the result is what is expected.
     */
    private void callGetter(String fieldName, Method getter, Object instance, Object expected)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        final Object getResult = getter.invoke(instance);

        if (getter.getReturnType().isPrimitive()) {
            /* Calling assetEquals() here due to autoboxing of primitive to object type. */
            assertEquals(fieldName + " is different", expected, getResult);
        } /*else {
            *//* This is a normal object. The object passed in should be the exactly same object we get back. *//*
            assertSame(fieldName + " is different", expected, getResult);
        }*/
    }

    /**
     * Creates an object for the given Class
     */
    private Object createObject(String fieldName, Class<?> clazz)
            throws InstantiationException, IllegalAccessException {

        try {
            final Supplier<?> supplier = this.mappers.get(clazz);
            if (supplier != null) {
                return supplier.get();
            }

            if (clazz.isEnum()) {
                return clazz.getEnumConstants()[0];
            }

            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Unable to create objects for field '" + fieldName + "'.", e);
        }
    }

    /**
     * Returns an instance to use to test the get and set methods.
     */
    protected abstract Set<Class<?>> getInstance();

    /**
     * Tests all the getters and setters.
     */
    @Test
    public void testGettersAndSetters() throws Exception {
        /* Sort items for consistent test runs. */

        final Set<Class<?>> instanceList = getInstance();
        for (Class<?> type : instanceList) {
            final SortedMap<String, GetterSetterPair> getterSetterMapping = new TreeMap<>();
            Object instance =null;
            if(type.getName().equals("pkgName.crewflip.dto.CrewFlipGetResponse")
            ||type.getName().equals("pkgName.crewflip.dto.CrewFlipGetResponse$CrewFlipGetResponseBuilder")){
                continue;
            }
            if(type.getName().equals("pkgName.Bean")){
                continue;
            }
            if(type.getName().equals("pkgName.OriginalBean")){
                continue;
            }
            
            if(type.getName().equals("pkgName.dto.Bean")
                    ||type.getName().equals("pkgName.BeanBuilder")){
                continue;
            }
           

            try {
                instance = type.newInstance();
            }
            catch(Exception e){
                continue;
            }
            for (final Method method : instance.getClass().getMethods()) {
                final String methodName = method.getName();

                if (this.ignoredGetFields.contains(methodName)) {
                    continue;
                }

                String objectName;
                if (methodName.startsWith("get") && method.getParameters().length == 0) {
                /* Found the get method. */
                    objectName = methodName.substring("get".length());

                    GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                    if (getterSettingPair == null) {
                        getterSettingPair = new GetterSetterPair();
                        getterSetterMapping.put(objectName, getterSettingPair);
                    }
                    getterSettingPair.setGetter(method);
                } else if (methodName.startsWith("set") && method.getParameters().length == 1) {
                /* Found the set method. */
                    objectName = methodName.substring("set".length());

                    GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                    if (getterSettingPair == null) {
                        getterSettingPair = new GetterSetterPair();
                        getterSetterMapping.put(objectName, getterSettingPair);
                    }
                    getterSettingPair.setSetter(method);
                } else if (methodName.startsWith("is") && method.getParameters().length == 0) {
                /* Found the is method, which really is a get method. */
                    objectName = methodName.substring("is".length());

                    GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                    if (getterSettingPair == null) {
                        getterSettingPair = new GetterSetterPair();
                        getterSetterMapping.put(objectName, getterSettingPair);
                    }
                    getterSettingPair.setGetter(method);
                }
            }


        /*
         * Found all our mappings. Now call the getter and setter or set the field via reflection and call the getting
         * it doesn't have a setter.
         */
            for (final Entry<String, GetterSetterPair> entry : getterSetterMapping.entrySet()) {
                final GetterSetterPair pair = entry.getValue();
                final String fieldName;

                final String objectName = entry.getKey();
                //final String fieldName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
                if(objectName.equals(objectName.toUpperCase())) {
                    fieldName = objectName.toLowerCase();
                }
                else {
                    fieldName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
                }
                if (pair.hasGetterAndSetter()) {
                /* Create an object. */
                    final Class<?> parameterType = pair.getSetter().getParameterTypes()[0];
                    final Object newObject = createObject(fieldName, parameterType);
                    try {
                        pair.getSetter().invoke(instance, newObject);
                    }
                    catch(Exception e ){
                        System.out.println(instance.getClass());
                    }
                    callGetter(fieldName, pair.getGetter(), instance, newObject);
                }
//                else if (pair.getGetter() != null) {
//                    final Object newObject = createObject(fieldName, pair.getGetter().getReturnType());
//                    final Field field = instance.getClass().getDeclaredField(fieldName);
//                    field.setAccessible(true);
//                    field.set(instance, newObject);
//
//                    callGetter(fieldName, pair.getGetter(), instance, newObject);
//                }
            }
        }
    }
}

//==================================
