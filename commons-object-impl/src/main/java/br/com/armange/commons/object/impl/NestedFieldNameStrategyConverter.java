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

import br.com.armange.commons.object.api.beanconverter.StrategicBeanConverter;
import br.com.armange.commons.object.api.beanconverter.StrategicBeanConverterWriter;

class NestedFieldNameStrategyConverter<S, T> implements StrategicBeanConverter<S, T> {

    @Override
    public StrategicBeanConverterWriter<S, T> readSource(final S sourceObject, final List<Field> sourceFields) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeInto(final T targetObject, final List<Field> targetFields) {
        // TODO Auto-generated method stub
        
    }

}
