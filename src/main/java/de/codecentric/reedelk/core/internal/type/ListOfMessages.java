package de.codecentric.reedelk.core.internal.type;

import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.util.ArrayList;

@Type(listItemType = Message.class)
public class ListOfMessages extends ArrayList<Message> {
}
