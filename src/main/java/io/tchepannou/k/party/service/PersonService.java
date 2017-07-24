package io.tchepannou.k.party.service;

import io.tchepannou.k.party.CreatePersonRequest;
import io.tchepannou.k.party.CreatePersonResponse;
import io.tchepannou.k.party.GetPersonResponse;
import io.tchepannou.k.party.UpdatePersonRequest;
import io.tchepannou.k.party.UpdatePersonResponse;
import io.tchepannou.k.party.dao.PersonDao;
import io.tchepannou.k.party.domain.Person;
import io.tchepannou.k.party.domain.Party;
import io.tchepannou.k.party.domain.PartyType;
import io.tchepannou.k.party.exception.Error;
import io.tchepannou.k.party.exception.InvalidRequestException;
import io.tchepannou.k.party.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class PersonService extends BasePartyService {
    private static final String BIRTHDATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    PersonDao personDao;

    @Transactional
    public CreatePersonResponse create(CreatePersonRequest request) {
        final Party party = createParty(PartyType.ID_PERSON);

        final Person person = new Person();
        person.setId(party.getId());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setGender(toGender(request.getGender()));
        person.setBirthDate(toBirthdate(request.getBirthDate()));
        personDao.save(person);

        final CreatePersonResponse response = new CreatePersonResponse();
        response.setId(person.getId());
        return response;
    }

    @Transactional
    public UpdatePersonResponse update(Integer id, UpdatePersonRequest request) {
        final Person person = personDao.findOne(id);
        if (person == null){
            throw new NotFoundException(Error.PARTY_NOT_FOUND);
        }

        person.setId(person.getId());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setGender(toGender(request.getGender()));
        person.setBirthDate(toBirthdate(request.getBirthDate()));
        personDao.save(person);

        final UpdatePersonResponse response = new UpdatePersonResponse();
        response.setId(person.getId());
        return response;
    }

    public GetPersonResponse findById(Integer id){
        final Person person = personDao.findOne(id);
        if (person == null){
            throw new NotFoundException(Error.PARTY_NOT_FOUND);
        }

        final PartyType partyType = partyTypeDao.findOne(PartyType.ID_PERSON);
        final io.tchepannou.k.party.PartyType type = new io.tchepannou.k.party.PartyType();
        type.setId(partyType.getId());
        type.setName(partyType.getName());

        final GetPersonResponse response = new GetPersonResponse();
        response.setId(person.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setGender(toGenderString(person.getGender()));
        response.setBirthDate(toBirthdateString(person.getBirthDate()));
        response.setPartyType(type);

        return response;
    }

    private Date toBirthdate(final String birthDate) {
        if (birthDate == null){
            return null;
        }
        try {
            return new SimpleDateFormat(BIRTHDATE_FORMAT, Locale.US).parse(birthDate);
        } catch (ParseException e){
            throw new InvalidRequestException(Error.INVALID_BIRTHDATE_FORMAT, e);
        }
    }

    private String toBirthdateString(final Date birthDate) {
        if (birthDate == null){
            return null;
        }
        return new SimpleDateFormat(BIRTHDATE_FORMAT, Locale.US).format(birthDate);
    }

    private Integer toGender(final String gender) {
        if (gender == null){
            return null;
        }
        else if ("M".equalsIgnoreCase(gender)){
            return Person.GENDER_MALE;
        } else if ("F".equalsIgnoreCase(gender)){
            return Person.GENDER_FEMALE;
        } else {
            throw new InvalidRequestException(Error.INVALID_GENDER);
        }
    }

    private String toGenderString(final Integer gender){
        if (gender == null){
            return null;
        } else if (gender.intValue() == Person.GENDER_MALE){
            return "M";
        } else if (gender.intValue() == Person.GENDER_FEMALE){
            return "F";
        }
        return null;
    }
}
