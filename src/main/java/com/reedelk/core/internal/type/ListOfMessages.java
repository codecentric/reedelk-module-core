package com.reedelk.core.internal.type;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.message.Message;

import java.util.ArrayList;

@Type(listItemType = Message.class)
public class ListOfMessages extends ArrayList<Message> {
}
