"use strict"

const DATA_ROOT = './data/';

module.exports = {

    metadata: () => ({
        "name": "Recommend",
        "properties": {
            "kind": { "type": "string", "required": true },
        },
        "supportedActions": [
        ]
    }),

    invoke: (conversation, done) => {
        const kind = conversation.properties().kind;

        const list = require(DATA_ROOT + 'udx.json');
        const filteredList = list.filter((element, idx, array) => {
            return element.kind === kind;
        });

        var shop = filteredList[Math.floor(Math.random() * filteredList.length)];

        conversation.logger().info("Kind: " + kind + ", shops: " + filteredList + ", Shop: " + shop);

        conversation.reply({ text: shop.name });

        conversation.transition();

        done();
    }
};
