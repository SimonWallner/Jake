package com.jakeapp.core.domain.logentries;

import com.jakeapp.core.domain.JakeObject;
import com.jakeapp.core.domain.UserId;
import com.jakeapp.core.domain.LogAction;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;


@Entity
@DiscriminatorValue(value = "JAKE_OBJECT_LOCK")
public class JakeObjectLockLogEntry extends JakeObjectLogEntry {
    public JakeObjectLockLogEntry(JakeObject belongsTo, UserId member, String comment, String checksum, Boolean processed) {
        super(LogAction.JAKE_OBJECT_LOCK, belongsTo, member, comment, checksum, processed);
    }

    public JakeObjectLockLogEntry() {
        setLogAction(LogAction.JAKE_OBJECT_LOCK);
    }
}