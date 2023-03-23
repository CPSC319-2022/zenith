const axios = require('axios');

const webhookUrl = 'https://hooks.slack.com/services/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';

async function sendBuildSuccessNotification(buildId, projectId) {
    const message = {
        text: `Build *${buildId}* for project *${projectId}* has succeeded.`,
    };

    try {
        await axios.post(webhookUrl, message);
        console.log('Build success notification sent to Slack.');
    } catch (error) {
        console.error('Error sending build success notification to Slack:', error);
    }
}

exports.notifySlack = async (req, res) => {
    const build = req.body;
    const projectId = build.projectId;
    const buildId = build.id;

    if (build.status === 'SUCCESS') {
        await sendBuildSuccessNotification(buildId, projectId);
    }

    res.sendStatus(200);
};
