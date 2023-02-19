package com.amcglynn.myenergi.aws.intentHandlers;

import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseVerifier {

    public static void verifySpeechInResponse(Response response, String speechText) {
        assertThat(response.getOutputSpeech()).isInstanceOf(SsmlOutputSpeech.class);
        var ssmlOutputSpeech = (SsmlOutputSpeech) response.getOutputSpeech();
        assertThat(ssmlOutputSpeech.getSsml())
                .isEqualTo(speechText);
    }

    public static void verifySimpleCardInResponse(Response response, String expectedTitle, String expectedContent) {
        var card = response.getCard();
        assertThat(card).isInstanceOf(SimpleCard.class);
        var simpleCard = (SimpleCard) card;
        assertThat(simpleCard.getTitle()).isEqualTo(expectedTitle);
        assertThat(simpleCard.getContent()).isEqualTo(expectedContent);
    }
}
