package com.utsman.universitylist.ui.homepage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import com.utsman.universitylist.data.University

/**
 * Composable that displays the content of a single university item in the list.
 *
 * @param modifier Modifier for styling.
 * @param university The [University] DTO to display.
 * @param onClick Callback invoked when the item is clicked.
 */
@Composable
fun UniversityItemContent(
    modifier: Modifier,
    university: University,
    onClick: (University) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick.invoke(university) }
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {

        // References for the UI elements
        val (image, title, domain) = createRefs()

        AsyncImage(
            model = university.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(80.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            text = university.name,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(image.top)
                    start.linkTo(image.end, margin = 16.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = university.domain,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .constrainAs(domain) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end, margin = 12.dp)
                    width = Dimension.fillToConstraints
                }
        )
    }

}

/**
 * Preview of the [UniversityItemContent] composable.
 */
@Composable
@Preview(showBackground = true)
fun UniversityItemContentPreview() {
    val sampleUniversity = University(
        name = "Sample University",
        domain = "sample.edu",
        webPage = "https://www.sample.edu",
        imageUrl = "https://placehold.co/600x400/green/white?text=SU"
    )
    UniversityItemContent(
        modifier = Modifier.fillMaxWidth(),
        university = sampleUniversity,
        onClick = {}
    )
}