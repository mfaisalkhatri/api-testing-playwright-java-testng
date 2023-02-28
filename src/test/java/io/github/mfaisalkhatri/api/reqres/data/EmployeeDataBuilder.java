package io.github.mfaisalkhatri.api.reqres.data;

import net.datafaker.Faker;

/**
 * @author Faisal Khatri
 * @since 2/28/2023
 **/
public class EmployeeDataBuilder {

    public static EmployeeData getEmployeeData () {
        Faker faker = new Faker ();
        return EmployeeData.builder ()
            .name (faker.name ()
                .firstName ())
            .job (faker.job ()
                .title ())
            .build ();

    }
}
