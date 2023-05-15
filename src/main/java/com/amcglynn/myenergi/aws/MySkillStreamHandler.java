package com.amcglynn.myenergi.aws;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amcglynn.myenergi.aws.intenthandlers.ChargeMyCarIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.FallbackIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.GetEnergyCostIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.GetEnergyUsageIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.GetPlugStatusIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.GoGreenIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.LaunchHandler;
import com.amcglynn.myenergi.aws.intenthandlers.SetChargeModeIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.StartBoostIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.StopBoostIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.StopIntentHandler;
import com.amcglynn.myenergi.aws.intenthandlers.ZappiSummaryIntentHandler;
import com.amcglynn.myenergi.service.ZappiServiceFactory;

public class MySkillStreamHandler extends SkillStreamHandler {

    public MySkillStreamHandler() {
        this(new ZappiServiceFactory());
    }

    public MySkillStreamHandler(ZappiServiceFactory factory) {
        super(Skills.standard()
                .addRequestHandler(new LaunchHandler())
                .addRequestHandler(new FallbackIntentHandler())
                .addRequestHandler(new StartBoostIntentHandler(factory))
                .addRequestHandler(new ZappiSummaryIntentHandler(factory))
                .addRequestHandler(new StopBoostIntentHandler(factory))
                .addRequestHandler(new SetChargeModeIntentHandler(factory))
                .addRequestHandler(new ChargeMyCarIntentHandler(factory))
                .addRequestHandler(new GoGreenIntentHandler(factory))
                .addRequestHandler(new GetPlugStatusIntentHandler(factory))
                .addRequestHandler(new GetEnergyUsageIntentHandler(factory))
                .addRequestHandler(new GetEnergyCostIntentHandler())
                .addRequestHandler(new StopIntentHandler())
                .build());
    }
}
