package fi.oamk.cottagerepublic.data

data class FAQDataModel(
    val id: String,
    val question: String,
    val answer: String)
object FAQ {
    var items = arrayListOf(
        FAQDataModel(
            "1",
            "What is Cottage Republic?",
            "Cottage Republic is the leading mobile platform for renting cabins"
        ),
        FAQDataModel(
            "2",
            "How much does posting a cabin cost?",
            "It is free to post cabins to the system ,we only take our margin on cabin's rented"
        ),
        FAQDataModel(
            "3",
            "Can I reserve for a weekend?",
            "Yes, its is possible to reserve cabin for 1 night. 14.00 - 12.00 next day."
        ),
        FAQDataModel(
            "4",
            "Can I reserve for a company?",
            "Yes, its is possible to reserve for a company. Please contact our costumer support if you need a bill."
        ),
        FAQDataModel(
            "5",
            "What happens when I press reserve?",
            "After we have received your payment we will send you the address and key pick up information, Please be sure to contact Cabin owner/maintainer 2-3 days before arrival."
        ),
        FAQDataModel(
            "6",
            "What do I take with me to cabin?",
            "Cabins include basic cooking equipment, electricity, gas, firewood are included in price if it is not other ways mentioned. Please take the bed linens and towels with you, but some cabin do rent them too."
        ),
        FAQDataModel(
            "7",
            "Can I bring my pet?",
            "If the owner has marked the cabin as pet cabin it is allowed."
        ),
        FAQDataModel(
            "8",
            "Can I reserve for a weekend?",
            "Yes, its is possible to reserve cabin for 1 night. 14.00 - 12.00 nex day."
        ),
        FAQDataModel(
            "9",
            "Does the cabin include a barbecue?",
            "It is an additional feature, Please check you cabin information to know if your cabin has one."
        ),
        FAQDataModel(
            "10",
            "How large fee does Cottage Republic take?",
            "We take 15% of the renting price but we have many different kind of limited time offers and special deals. Please don't hesitate to contact our customer support for more information"
        ),
    )
}