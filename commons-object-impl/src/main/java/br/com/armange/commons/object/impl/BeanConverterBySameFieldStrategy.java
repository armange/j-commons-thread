/*
 * Copyright [2019] [Diego Armange Costa]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */
package br.com.armange.commons.object.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import br.com.armange.commons.reflection.support.FieldSupport;

class BeanConverterBySameFieldStrategy<S, T> {

    private final S sourceObject;
    private final List<Field> sourceFields;
    
    BeanConverterBySameFieldStrategy(final S sourceObject, final List<Field> sourceFields) {
        this.sourceObject = sourceObject;
        this.sourceFields = sourceFields;
    }
    
    static <S, T> BeanConverterBySameFieldStrategy<S, T> readSource(final S sourceObject, final List<Field> sourceFields) {
        return new BeanConverterBySameFieldStrategy<S,T>(sourceObject, sourceFields);
    }
    
    void writeInto(final T targetObject, final List<Field> targetFields) {
        targetFields
            .parallelStream()
            .forEach(forEachSourceFieldDoSameFieldConsumer(targetObject));
    }

    private Consumer<? super Field> forEachSourceFieldDoSameFieldConsumer(final T targetObject) {
        return targetField -> {
            sourceFields.parallelStream().forEach(sameFieldConsumer(targetObject, targetField));
        };
    }
    
    private Consumer<? super Field> sameFieldConsumer(final Object targetObject, final Field targetField) {
        return sourceField -> { 
            if (sourceField.getName().equals(targetField.getName())) {
                copyFieldValue(targetObject, targetField, sourceField);
            }
        };
    }
    
    private void copyFieldValue(final Object targetObject, final Field targetField, final Field sourceField) {
        FieldSupport.from(targetField).setValue(targetObject, FieldSupport.from(sourceField).getValue(sourceObject));
    }
}
