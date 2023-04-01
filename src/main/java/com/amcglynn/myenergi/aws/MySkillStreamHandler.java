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
import com.amcglynn.myenergi.aws.intenthandlers.ZappiSummaryIntentHandler;
import com.amcglynn.myenergi.service.ZappiService;

public class MySkillStreamHandler extends SkillStreamHandler {

    public MySkillStreamHandler() {
        this(new ZappiService());
    }

    public MySkillStreamHandler(ZappiService zappiService) {
        super(Skills.standard()
                .addRequestHandler(new LaunchHandler())
                .addRequestHandler(new FallbackIntentHandler())
                .addRequestHandler(new StartBoostIntentHandler(zappiService))
                .addRequestHandler(new ZappiSummaryIntentHandler(zappiService))
                .addRequestHandler(new StopBoostIntentHandler(zappiService))
                .addRequestHandler(new SetChargeModeIntentHandler(zappiService))
                .addRequestHandler(new ChargeMyCarIntentHandler(zappiService))
                .addRequestHandler(new GoGreenIntentHandler(zappiService))
                .addRequestHandler(new GetPlugStatusIntentHandler(zappiService))
                .addRequestHandler(new GetEnergyUsageIntentHandler(zappiService))
                .addRequestHandler(new GetEnergyCostIntentHandler(zappiService))
                .build());
    }
}
